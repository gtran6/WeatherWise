package com.myprojects.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.data.remote.Resource
import com.myprojects.data.remote.Status
import com.myprojects.model.FavoriteModel
import com.myprojects.ui.adpater.FavoriteAdapter
import com.myprojects.ui.home.HomeFragmentArgs
import com.myprojects.weatherwise.MainActivity
import com.myprojects.weatherwise.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(), FavoriteAdapter.FavoriteAdapterInterface {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var navController: NavController
    private lateinit var cashedData: ArrayList<FavoriteEntity>
    private val viewModel by viewModel<FavoriteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.btnAdd.setOnClickListener {
            navController.navigate(FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment(false))
        }
        viewModel.getFavoriteList().observe(viewLifecycleOwner) { setupLayout(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupLayout(response: Resource<List<FavoriteEntity>>) {
        when (response.status) {
            Status.LOADING -> {

                binding.progressBar.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                if (response.data != null && !response.data.isEmpty()) {
                    cashedData = response.data as ArrayList<FavoriteEntity>
                    binding.progressBar.visibility = View.GONE
                    binding.layout.visibility = View.GONE
                    binding.data =
                        FavoriteModel(this, response.data)
                    binding.executePendingBindings()
                } else {
                    binding.layout.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                }
            }
            Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                binding.layout.visibility = View.VISIBLE

            }
        }
    }

    override fun onResume() {
        super.onResume()
        val act = requireActivity() as MainActivity
        act.setUpNavigationWithHamburger()
    }

    override fun onDeleteImageClick(pos: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Alert")
            .setMessage("Do you want to delete the location?")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Delete") { dialog, which ->
                viewModel.deleteFromFavorite(cashedData[0])
                cashedData.removeAt(pos)
                binding.data = FavoriteModel(this, cashedData)
                binding.executePendingBindings()
                if (cashedData.isEmpty()) {
                    binding.layout.visibility = View.VISIBLE
                }
            }.show()
    }

    override fun onItemClick(pos: Int) {
        val data = cashedData[pos].cashedData
        navController.navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToNavHome(
                data = data,
                latlog = LatLng(data.lat, data.lon)
            )
        )
        val act = requireActivity() as MainActivity
        act.setUpNavigationWithNoHamburger()
    }
}