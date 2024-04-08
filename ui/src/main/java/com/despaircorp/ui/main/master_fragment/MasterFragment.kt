package com.despaircorp.ui.main.master_fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.FragmentMasterBinding
import com.despaircorp.ui.main.master_fragment.estate.EstateAdapter
import com.despaircorp.ui.main.master_fragment.estate.EstateListener
import com.despaircorp.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MasterFragment : Fragment(R.layout.fragment_master), EstateListener {
    private val binding by viewBinding { FragmentMasterBinding.bind(it) }
    private val viewModel: MasterViewModel by viewModels()
    private var listener: OnItemSelectedListener? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = EstateAdapter(this)
        binding.fragmentMasterRecyclerViewEstate.adapter = adapter
        
        viewModel.viewState.observe(viewLifecycleOwner) {
            adapter.submitList(it.estateViewStateItems)
        }
    }
    
    interface OnItemSelectedListener {
        fun onItemSelected(itemId: Int)
    }
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnItemSelectedListener")
        }
    }
    
    override fun onEstateClicked(id: Int) {
        listener?.onItemSelected(id)
        viewModel.onEstateClicked(id)
    }
    
}