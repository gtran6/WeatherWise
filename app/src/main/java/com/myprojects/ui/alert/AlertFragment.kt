package com.myprojects.ui.alert

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myprojects.data.entity.AlertEntity
import com.myprojects.data.remote.Resource
import com.myprojects.data.remote.Status
import com.myprojects.model.AlertModel
import com.myprojects.ui.adapter.AlertAdapter
import com.myprojects.weatherwise.R
import com.myprojects.weatherwise.databinding.FragmentAlertBinding
import com.myprojects.worker.AddAlertRemainder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AlertFragment : Fragment(), AlertAdapter.AlertAdapterListener {

    private lateinit var list: ArrayList<AlertEntity>
    private lateinit var binding: FragmentAlertBinding
    private var startDate: Long = 0
    private var endDate: Long = 0

    private val viewModel by viewModel<AlertViewModel>()

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllAlerts().observe(viewLifecycleOwner) {
            setupLayout(it)
        }
        binding.btnAdd.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.alert_dialog)
            val btnSave = dialog.findViewById<Button>(R.id.btnSave)
            val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
            val txtStatDate = dialog.findViewById<TextView>(R.id.txtStartDate)
            val txtStatTime = dialog.findViewById<TextView>(R.id.txtStartTime)
            val txtEndDate = dialog.findViewById<TextView>(R.id.txtEndDate)
            val txtEndTime = dialog.findViewById<TextView>(R.id.txtEndTime)
            val cardStart = dialog.findViewById<CardView>(R.id.cardStart)
            val cardEnd = dialog.findViewById<CardView>(R.id.cardEnd)

            cardEnd.setOnClickListener {
                val endDate = Calendar.getInstance()

                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        endDate.set(Calendar.YEAR, year)
                        endDate.set(Calendar.MONTH, monthOfYear)
                        endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        txtEndDate.text = SimpleDateFormat("dd MMM").format(endDate.time)
                    }

                DatePickerDialog(
                    requireContext(), dateSetListener,
                    endDate.get(Calendar.YEAR),
                    endDate.get(Calendar.MONTH),
                    endDate.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            cardStart.setOnClickListener {
                val startDate = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        startDate.set(Calendar.HOUR_OF_DAY, hour)
                        startDate.set(Calendar.MINUTE, minute)
                        txtStatTime.text = SimpleDateFormat("hh:mm aaa").format(startDate.time)
                        this.startDate = startDate.timeInMillis
                    }

                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        startDate.set(Calendar.YEAR, year)
                        startDate.set(Calendar.MONTH, monthOfYear)
                        startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        txtStatDate.text = SimpleDateFormat("dd MMM").format(startDate.time)
                        TimePickerDialog(
                            context,
                            timeSetListener,
                            startDate.get(Calendar.HOUR_OF_DAY),
                            startDate.get(Calendar.MINUTE),
                            false
                        ).show()
                    }

                DatePickerDialog(
                    requireContext(), dateSetListener,
                    startDate.get(Calendar.YEAR),
                    startDate.get(Calendar.MONTH),
                    startDate.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            btnCancel.setOnClickListener { dialog.dismiss() }
            btnSave.setOnClickListener {
                val pojo = AlertEntity(
                    startDate = startDate,
                    endDate = endDate
                )
                viewModel.insetAlert(
                    pojo
                )
                binding.emptyLayout.visibility = View.GONE
                viewModel.getAllAlerts().observe(viewLifecycleOwner) { setupLayout(it) }
                AddAlertRemainder.addPeriodicAlert(
                    delay = startDate,
                    context = requireContext(),
                    workerTag = pojo.id.toString()
                )
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun setupLayout(it: Resource<List<AlertEntity>>?) {
        if (it != null) {
            if (!it.data.isNullOrEmpty())
                list =
                    it.data as ArrayList<AlertEntity>
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data.isNullOrEmpty()) {
                        binding.emptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.data = AlertModel(
                            it.data as ArrayList<AlertEntity>,
                            this
                        )
                        binding.executePendingBindings()
                    }
                }
                Status.ERROR -> {
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDeleteImageClick(pos: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete alert")
            .setMessage("Do you want to delete alert?")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Delete") { _, _ ->
                viewModel.removeAlert(list[pos])
                AddAlertRemainder.removeWorkers(
                    context = requireContext(),
                    workerTag = list[pos].id.toString()
                )
                list.removeAt(pos)
                binding.data = AlertModel(list, this)
                binding.executePendingBindings()
                if (list.isEmpty()) {
                    binding.emptyLayout.visibility = View.VISIBLE
                }
            }.show()
    }
}