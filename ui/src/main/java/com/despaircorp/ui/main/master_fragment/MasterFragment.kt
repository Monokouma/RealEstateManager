package com.despaircorp.ui.main.master_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.FragmentMasterBinding
import com.despaircorp.ui.main.details_fragment.DetailFragment
import com.despaircorp.ui.main.master_fragment.estate.EstateAdapter
import com.despaircorp.ui.main.master_fragment.estate.EstateListener
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MasterFragment : Fragment(R.layout.fragment_master), EstateListener {
    private val binding by viewBinding { FragmentMasterBinding.bind(it) }
    private val viewModel: MasterViewModel by viewModels()
    
    private lateinit var navController: NavController
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        
        navController = Navigation.findNavController(
            requireActivity(),
            R.id.activity_main_FragmentContainer_view
        )
        
        
        val adapter = EstateAdapter(this)
        binding.fragmentMasterRecyclerViewEstate.adapter = adapter
        
        viewModel.viewState.observe(viewLifecycleOwner) {
            adapter.submitList(it.estateViewStateItems)
            
        }
        
    }
    
    override fun onResume() {
        super.onResume()
        // true only in landscape
        
        if (binding.detailFragmentContainer != null && childFragmentManager.findFragmentById(R.id.detailFragment) == null) {
            // Create new DetailFragment only if there isn't one already
            childFragmentManager.beginTransaction()
                .replace(binding.detailFragmentContainer!!.id, DetailFragment())
                .commit()
        }
    }
    
    override fun onEstateClicked(id: Int) {
        navController.navigate(MasterFragmentDirections.actionMasterFragmentToDetailFragment(id))
        viewModel.onEstateClicked(id)
        
    }
    
    companion object {
        private const val ARG_ESTATE_ID = "ARG_ESTATE_ID"
    }
}