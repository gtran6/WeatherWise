package com.myprojects.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.myprojects.data.remote.Resource
import com.myprojects.data.remote.Status
import com.myprojects.model.weather.WeatherResponse
import com.myprojects.weatherwise.R
import com.myprojects.weatherwise.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    private val permissions: MutableList<String> = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val fusedLocationProviderClient by lazy {
        FusedLocationProviderClient(requireActivity())
    }
    companion object {
        fun newInstance() = SplashFragment()
    }

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().lifecycleScope.launchWhenCreated {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), permissions[0]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), permissions[1]
            ) == PackageManager.PERMISSION_GRANTED
        )
            getLocation()
        else
            dinedPermission()
    }

    private fun dinedPermission() {
        binding.txtHint.visibility = View.VISIBLE
        binding.animationView.repeatCount = 1
        binding.animationView.setAnimation(R.raw.select_location)
        binding.animationView.setOnClickListener {
            findNavController().navigate(
                SplashFragmentDirections.actionNavSplashToMapsFragment(
                    true
                )
            )
        }
        showSnackbar()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            if (result[permissions[0]] == true && result[permissions[1]] == true)
                getLocation()
            else
                dinedPermission()
        }

    private fun showSnackbar() {
        Snackbar.make(
            binding.parent,
            getString(R.string.premission_dined),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.ok)) {
            binding.txtHint.visibility = View.GONE
            binding.animationView.setAnimation(R.raw.newspl)
            binding.animationView.repeatCount = LottieDrawable.INFINITE
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }.show()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                saveLocation(LatLng(it.latitude, it.longitude))
            }
            else {
                requestLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest().apply {
                interval = 1000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_LOW_POWER
            },
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationRequest: LocationResult?) {
            if(locationRequest != null){
                val latLng = LatLng(
                    locationRequest.lastLocation.latitude,
                    locationRequest.lastLocation.longitude
                )
                saveLocation(latLng)
                fusedLocationProviderClient.removeLocationUpdates(this)
            } else {
            }
        }
    }

    private fun saveLocation(latLng: LatLng) {
        viewModel.setLatLon(latLng)
        if (view != null)
            viewModel.getDataFromRepo(latLng, viewModel.getLang())
                .observe(viewLifecycleOwner) { res ->
                    onResponse(res)
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
    }

    @SuppressLint("MissingPermission")
    private fun onResponse(res: Resource<WeatherResponse>?) {
        when (res!!.status) {
            Status.SUCCESS -> {
                viewModel.saveResponse(
                    res.data ?: WeatherResponse()
                )
                viewModel.setTimeStamp(System.currentTimeMillis())
                findNavController().navigate(
                    SplashFragmentDirections.actionNavSplashToNavHome(
                        data = res.data ?: WeatherResponse(),
                        latlog = viewModel.getLatLon()
                    )
                )
            }
            Status.ERROR -> {
                binding.retryBtn.isVisible = true
                binding.txtHint.isVisible = true
                binding.animationView.isVisible = true
                binding.animationView.setAnimation(R.raw.connection_error)
                binding.txtHint.text = res.message
                binding.animationView.pauseAnimation()
                binding.animationView.setPadding(40, 0, 40, 0)
                binding.retryBtn.setOnClickListener {
                    getLocation()
                }
            }
            Status.LOADING -> {
                binding.retryBtn.isVisible = false
                binding.txtHint.isVisible = false
                binding.animationView.setPadding(0, 0, 0, 0)
                binding.animationView.setAnimation(R.raw.newspl)
                binding.animationView.resumeAnimation()
            }
        }
    }
}