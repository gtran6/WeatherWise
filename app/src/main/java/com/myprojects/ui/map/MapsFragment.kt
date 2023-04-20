package com.myprojects.ui.map

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.data.preferences.NULL_LAT
import com.myprojects.data.preferences.NULL_LON
import com.myprojects.data.remote.Status
import com.myprojects.weatherwise.R
import com.myprojects.weatherwise.databinding.FragmentMapsBinding
import java.util.*

class MapsFragment : Fragment(), GoogleMap.OnMapClickListener {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var latLng: LatLng
    private val args: MapsFragmentArgs by navArgs()
    private val nullLatLon = LatLng(NULL_LAT, NULL_LON)

    private val viewModel by viewModels<MapViewModel>()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        map = googleMap
        with(map) {
            setOnMapClickListener(this@MapsFragment)
            uiSettings.setAllGesturesEnabled(true)
            googleMap.addMarker(MarkerOptions().position(viewModel.getMyLatLon()).title("Home"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.getMyLatLon(), 10f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMapClick(p0: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(latLng))

        if (args.itItMyLocation) {
            binding.btnSave.visibility = View.VISIBLE
            this.latLng = latLng
            if (viewModel.getMyLatLon() != nullLatLon)
                binding.btnSave.text = getString(R.string.update)

            binding.btnSave.setOnClickListener {
                viewModel.saveMyLatLon(latLng)
                findNavController().popBackStack()
                Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            val geocoder =
                Geocoder(requireContext().applicationContext, Locale(viewModel.getLang()))
            val address: MutableList<Address>? =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            viewModel.getWeatherRemotlyLatlon(latLng).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        map.setOnMapClickListener(null)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        map.setOnMapClickListener(this)
                        if (address != null)
                            viewModel.addFavoriteTodatabase(
                                FavoriteEntity(
                                    locationName = address[0].countryName + address[0].adminArea,
                                    latLng = latLng,
                                    cashedData = it.data!!
                                )
                            )
                        binding.progressBar.visibility = View.GONE
                        binding.btnSave.visibility = View.VISIBLE
                        binding.btnSave.setOnClickListener {
                            findNavController().popBackStack()
                            Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    Status.ERROR -> {
                    }
                }
            }
        }
    }
}