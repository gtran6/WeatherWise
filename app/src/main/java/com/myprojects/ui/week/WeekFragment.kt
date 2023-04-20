package com.myprojects.ui.week

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myprojects.model.ReportModel
import com.myprojects.model.weather.Daily
import com.myprojects.model.weather.Hourly
import com.myprojects.weatherwise.databinding.FragmentWeekBinding

class WeekFragment : Fragment() {
    private lateinit var binding: FragmentWeekBinding
    private lateinit var navController: NavController
    private val args: WeekFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.data = ReportModel(
            week = args.data.daily as ArrayList<Daily>,
            day = args.data.hourly as ArrayList<Hourly>
        )
        binding.executePendingBindings()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeekBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}