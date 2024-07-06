package com.example.lab4.ui.form

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lab4.R
import com.example.lab4.databinding.FragmentFormBinding
import com.example.lab4.ui.base.BaseFragment
import com.example.lab4.ui.extensions.toastLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FormFragment : BaseFragment<FragmentFormBinding>(), View.OnClickListener {

    private val formViewModel: FormViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun inflateBinding() {
        binding = FragmentFormBinding.inflate(layoutInflater)
    }

    override fun createViewAfterInflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setUpListeners()

        locationCallback = object : LocationCallback() {
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    binding?.tvLatitude?.text = "Latitud: $lat"
                    binding?.tvLongitude?.text = "Longitud: $lon"
                    Log.d(TAG, "Latitud: $lat, Longitud: $lon")
                }
            }
        }
    }

    private val callRequestPermissionLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLocation()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "l> Permiso no concedido")
            }
            Log.d(TAG, "l> Permiso denegado")
        }
    }

    private fun askPermissionLocation() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "l> Permiso ya concedido")
                getLocation()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Log.d(TAG, "l> Permiso de localización no concedido, volver a solicitar")
                callRequestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            else -> {
                Log.d(TAG, "l> Permiso localización no concedido, lo volvemos a solicitar")
                callRequestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun setUpListeners() {
        binding?.btLocation?.setOnClickListener(this)
        binding?.btSave?.setOnClickListener(this)
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.register), showBack = true)
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.loadingFlow.collect {
                showLoading(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.errorFlow.collect { error ->
                requireContext().toastLong(error.message)
            }
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) =
        Unit

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btLocation -> {
                askPermissionLocation()
            }

            R.id.btSave -> {
                formViewModel.addUser(
                    binding?.etName?.text.toString(),
                    binding?.etColor?.text.toString(),
                    binding?.etBirthDate?.text.toString(),
                    binding?.etCity?.text.toString(),
                    binding?.etNumber?.text.toString().toInt(),
                    binding?.tvLatitude?.text.toString().toDouble(),
                    binding?.tvLongitude?.text.toString().toDouble()
                )
                hideKeyboard()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        binding?.tvLatitude?.text = "$lat"
                        binding?.tvLongitude?.text = "$lon"
                        Log.d(TAG, "Latitud: $lat, Longitud: $lon")
                    } else {
                        binding?.tvLatitude?.text = "Location not available"
                        Log.d(TAG, "Location not available, requesting new location")
                        startLocationUpdates()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to get location")
                }
        } else {
            callRequestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(20000)
            .build()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}