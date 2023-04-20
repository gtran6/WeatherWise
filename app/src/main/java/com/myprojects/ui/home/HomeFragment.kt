package com.myprojects.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myprojects.data.entity.CashedEntity
import com.myprojects.data.remote.Resource
import com.myprojects.data.remote.Status
import com.myprojects.model.HomeModel
import com.myprojects.model.weather.Hourly
import com.myprojects.model.weather.WeatherResponse
import com.myprojects.util.getDate
import com.myprojects.weatherwise.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private lateinit var navController: NavController
    private val args: HomeFragmentArgs by navArgs()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var cashedData: WeatherResponse
    private val viewModel by viewModel<HomeViewModel>()
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val data = args.data

        if (data != null) {
            setupLayout(data)
        } else {
            viewModel.getCashedData().observe(viewLifecycleOwner) { checkStatus(it) }
        }

        binding.btnViewFullReport.setOnClickListener {
            navController.navigate(
                HomeFragmentDirections.actionNavHomeToWeekFragment(
                    args.data ?: cashedData
                )
            )
        }

    }

    private fun checkStatus(it: Resource<List<CashedEntity>>?) {
        when (it!!.status) {
            Status.SUCCESS -> if (!it.data.isNullOrEmpty()) setupLayout(it.data[0].cashedData)
            Status.ERROR -> Log.d(TAG, "onViewCreated: ${it.message}")
            Status.LOADING -> Log.d(TAG, "onViewCreated: LOADING")
        }
    }

    private fun setupLayout(weatherResponse: WeatherResponse) {
        weatherResponse.apply {
            cashedData = this
            binding.data = HomeModel(
                timezone,
                getDate(current.dt),
                current.weather[0].icon,
                current.temp,
                current.weather[0].description,
                current.windSpeed,
                current.humidity,
                current.feelsLike,
                hourly as ArrayList<Hourly>
            )
            binding.latlog = args.latlog
            binding.executePendingBindings()
        }
    }

}