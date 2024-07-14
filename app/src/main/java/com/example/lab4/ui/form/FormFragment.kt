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
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab4.R
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.databinding.FragmentFormBinding
import com.example.lab4.ui.base.BaseFragment
import com.example.lab4.ui.extensions.gone
import com.example.lab4.ui.extensions.invisible
import com.example.lab4.ui.extensions.toastLong
import com.example.lab4.ui.extensions.visible
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
    private val args: FormFragmentArgs by navArgs()
    private var isEditMode: Boolean = false

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

        isEditMode = args.id != -1L
        if (isEditMode) {
            formViewModel.getUserById(args.id.toInt())
            binding?.ibEdit?.visible()
            disableEdits()
        } else {
            binding?.btCity?.invisible()
        }
    }

    private val callRequestPermissionLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLocation()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showPermissionDeniedDialog()
            } else {
                requireContext().toastLong(getString(R.string.permission_denied))
            }
        }
    }

    private fun askPermissionLocation() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getLocation()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                callRequestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            else -> {
                callRequestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permiso necesario")
            .setMessage(getString(R.string.permission_message))
            .setPositiveButton("Configuración") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun setUpListeners() {
        binding?.btLocation?.setOnClickListener(this)
        binding?.btSave?.setOnClickListener(this)
        binding?.ibEdit?.setOnClickListener(this)
        binding?.btCity?.setOnClickListener(this)
    }

    override fun configureToolbarAndConfigScreenSections() {
        fragmentLayoutWithToolbar()
        showToolbar(title = getString(R.string.form_fragment_title), showBack = true)
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.loadingFlow.collect {
                showLoading(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.userStateFlow.collect { user ->
                user?.let { fillForm(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            formViewModel.errorFlow.collect { error ->
                requireContext().toastLong(error.message)
            }
        }
    }

    private fun fillForm(user: UserModel) {
        binding?.apply {
            etName.setText(user.name)
            etColor.setText(user.favoriteColor)
            etBirthDate.setText(user.birthDate)
            etCity.setText(user.favoriteCity)
            etNumber.setText(user.favoriteNumber.toString())
            tvLatitude.text = user.latitude.toString()
            tvLongitude.text = user.longitude.toString()
        }
    }

    override fun viewCreatedAfterSetupObserverViewModel(view: View, savedInstanceState: Bundle?) =
        Unit

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btLocation -> {
                askPermissionLocation()
            }

            R.id.ibEdit -> {
                enableEdits()
            }

            R.id.btSave -> {
                val name = binding?.etName?.text.toString()
                val color = binding?.etColor?.text.toString()
                val birthDate = binding?.etBirthDate?.text.toString()
                val city = binding?.etCity?.text.toString()
                val number = binding?.etNumber?.text.toString().toInt()
                val lat = binding?.tvLatitude?.text.toString().toDouble()
                val lon = binding?.tvLongitude?.text.toString().toDouble()

                if (isEditMode) {
                    val user = UserModel(
                        id = args.id.toInt(),
                        name = name,
                        favoriteColor = color,
                        birthDate = birthDate,
                        favoriteCity = city,
                        favoriteNumber = number,
                        latitude = lat,
                        longitude = lon
                    )
                    formViewModel.updateUser(user)
                    requireContext().toastLong("Usuario actualizado")
                    disableEdits()
                } else {
                    val user = UserModel(
                        name = name,
                        favoriteColor = color,
                        birthDate = birthDate,
                        favoriteCity = city,
                        favoriteNumber = number,
                        latitude = lat,
                        longitude = lon
                    )
                    formViewModel.addUser(user)
                    findNavController().navigateUp()
                }
                hideKeyboard()
            }

            R.id.btCity -> {
                val action =
                    FormFragmentDirections.actionFormFragmentToMapFragment(binding?.etCity?.text.toString())
                findNavController().navigate(action)
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
                    } else {
                        binding?.tvLatitude?.text = "Location not available"
                        startLocationUpdates()
                    }
                }
                .addOnFailureListener {
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

    private fun disableEdits() {
        binding?.etName?.isEnabled = false
        binding?.etColor?.isEnabled = false
        binding?.etBirthDate?.isEnabled = false
        binding?.etCity?.isEnabled = false
        binding?.etNumber?.isEnabled = false
        binding?.btLocation?.invisible()
        binding?.btSave?.isEnabled = false
        binding?.btCity?.visible()
    }

    private fun enableEdits() {
        binding?.etName?.isEnabled = true
        binding?.etColor?.isEnabled = true
        binding?.etBirthDate?.isEnabled = true
        binding?.etCity?.isEnabled = true
        binding?.etNumber?.isEnabled = true
        binding?.btLocation?.visible()
        binding?.btSave?.isEnabled = true
        binding?.btCity?.gone()
    }

}