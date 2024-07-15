package com.example.lab4.ui.map

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.lab4.BuildConfig
import com.example.lab4.R
import com.example.lab4.databinding.FragmentMapBinding
import com.example.lab4.ui.base.BaseFragment
import com.example.lab4.ui.extensions.toastLong
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private val mapViewModel: MapViewModel by viewModels()

    private lateinit var myGoogleMap: GoogleMap
    private val args: MapFragmentArgs by navArgs()
    private lateinit var placesClient: PlacesClient

    override fun inflateBinding() {
        binding = FragmentMapBinding.inflate(layoutInflater)
    }

    override fun createViewAfterInflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.fcvMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())

        searchCity(args.city)
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.map_fragment_title), showBack = true)
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.loadingFlow.collect {
                showLoading(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.errorFlow.collect { error ->
                requireContext().toastLong(error.message)
            }
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) =
        Unit

    override fun onMapReady(googleMap: GoogleMap) {
        myGoogleMap = googleMap
    }

    private fun searchCity(city: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(city)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val prediction = response.autocompletePredictions.firstOrNull()
            if (prediction != null) {
                val placeId = prediction.placeId

                val placeRequest =
                    FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build()
                placesClient.fetchPlace(placeRequest).addOnSuccessListener { placeResponse ->
                    val latLng = placeResponse.place.latLng
                    if (latLng != null) {
                        myGoogleMap.addMarker(MarkerOptions().position(latLng).title(city))
                        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    }
                }.addOnFailureListener {
                    requireContext().toastLong(getString(R.string.error_unknown_error))
                }
            } else {
                requireContext().toastLong(getString(R.string.city_not_found))
            }
        }.addOnFailureListener { error ->
            Log.e(TAG, "Error encontrando ciudad: ${error.message}", error)
        }
    }

}