package com.myprojects.ui.map

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment(), GoogleMap.OnMapClickListener {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var latLng: LatLng
    private val args: MapsFragmentArgs by navArgs()
    private val nullLatLon = LatLng(NULL_LAT, NULL_LON)

    private val viewModel: MapViewModel by viewModel()

    //private val viewModel by viewModels<MapViewModel>()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        map = googleMap
        with(map) {
            setOnMapClickListener(this@MapsFragment)
            uiSettings.setAllGesturesEnabled(true)
            googleMap.addMarker(MarkerOptions().position(viewModel.getMyLatLon()).title("My Home"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.getMyLatLon(), 10f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        map.addMarker(MarkerOptions().position(p0))

        if (args.itItMyLocation) {
            binding.btnSave.visibility = View.VISIBLE
            this.latLng = p0
            if (viewModel.getMyLatLon() != nullLatLon)
                binding.btnSave.text = getString(R.string.update)

            binding.btnSave.setOnClickListener {
                viewModel.saveMyLatLon(p0)
                findNavController().popBackStack()
                Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
            }
            Log.d("MapsFragment", "onMapClick: My Location clicked")
        } else {
            val geocoder =
                Geocoder(requireContext().applicationContext, Locale(viewModel.getLang()))
            val address: MutableList<Address>? =
                geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
            viewModel.getWeatherRemotelyLatlon(p0).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        map.setOnMapClickListener(null)
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("MapsFragment", "getWeatherRemotelyLatlon: LOADING")
                    }
                    Status.SUCCESS -> {
                        map.setOnMapClickListener(this)
                        if (address != null)
                            viewModel.addFavoriteToDatabase(
                                FavoriteEntity(
                                    locationName = address[0].countryName + address[0].adminArea,
                                    latLng = p0,
                                    cashedData = it.data!!
                                )
                            )
                        binding.progressBar.visibility = View.GONE
                        binding.btnSave.visibility = View.VISIBLE
                        binding.btnSave.setOnClickListener {
                            findNavController().popBackStack()
                            Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
                        }
                        Log.d("MapsFragment", "getWeatherRemotelyLatlon: SUCCESS")
                    }
                    Status.ERROR -> {
                        Log.e("MapsFragment", "getWeatherRemotelyLatlon: ERROR")
                    }
                }
            }
            Log.d("MapsFragment", "onMapClick: Other location clicked")
        }
    }
}