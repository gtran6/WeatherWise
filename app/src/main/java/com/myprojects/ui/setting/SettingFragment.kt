package com.myprojects.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.myprojects.data.preferences.AppUnits
import com.myprojects.weatherwise.databinding.FragmentSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
    }

    private fun setupLayout() {
        when (viewModel.getWindSpeedUnit()) {
            AppUnits.METER_BY_SECOND.string -> binding.mByS.isChecked = true
            AppUnits.MILE_BY_HOUR.string -> binding.mByH.isChecked = true
        }

        when (viewModel.getTempUnit()) {
            AppUnits.FAHRENHEIT.string -> binding.tempFahrenheit.isChecked = true
            AppUnits.KELVIN.string -> binding.tempKelvin.isChecked = true
            AppUnits.CELSIUS.string -> binding.tempCelsius.isChecked = true
        }

        binding.radioTemp.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.tempFahrenheit.id -> {
                    viewModel.setTempUnit(AppUnits.FAHRENHEIT.string)
                    Toast.makeText(requireContext(), binding.tempFahrenheit.text, Toast.LENGTH_SHORT).show()
                }

                binding.tempCelsius.id -> {
                    viewModel.setTempUnit(AppUnits.CELSIUS.string)
                    Toast.makeText(requireContext(), binding.tempCelsius.text, Toast.LENGTH_SHORT).show()
                }

                binding.tempKelvin.id -> {
                    viewModel.setTempUnit(AppUnits.KELVIN.string)
                    Toast.makeText(requireContext(), binding.tempKelvin.text, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.radioWind.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.mByH.id -> {
                    viewModel.setWindSpeedUnit(AppUnits.MILE_BY_HOUR.string)
                    Toast.makeText(
                        requireContext(),
                        binding.mByH.text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.mByS.id -> {
                    viewModel.setWindSpeedUnit(AppUnits.METER_BY_SECOND.string)
                    Toast.makeText(requireContext(), binding.mByS.text, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}