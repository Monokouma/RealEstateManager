package com.despaircorp.ui.main.estate_form.estate_type

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.EstateTypeItemViewBinding
import com.despaircorp.ui.utils.isNightMode


class EstateTypeAdapter(
    private val estateTypeListener: EstateTypeListener
) : ListAdapter<EstateTypeViewStateItems, EstateTypeAdapter.EstateTypeViewHolder>(
    PointOfInterestDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EstateTypeViewHolder(
        EstateTypeItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: EstateTypeViewHolder, position: Int) {
        holder.bind(getItem(position), estateTypeListener)
    }
    
    class EstateTypeViewHolder(private val binding: EstateTypeItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            item: EstateTypeViewStateItems,
            listener: EstateTypeListener
        ) {
            binding.estateTypeItemViewMaterialButtonButton.text =
                binding.root.context.getString(item.estateType.textRes)
            
            if (item.isFiltering) {
                if (isNightMode(binding.root.context)) {
                    if (item.isSelected) {
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.primaryColorDark)
                        )
                    } else {
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.unselectButtonDark)
                        )
                    }
                } else {
                    if (item.isSelected) {
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.primaryColorLight)
                        )
                    } else {
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.unselectButtonLight)
                        )
                    }
                }
                
                binding.estateTypeItemViewMaterialButtonButton.setOnClickListener {
                    if (item.isSelected) {
                        listener.onRemoveEstateTypeClicked(item.id)
                    } else {
                        listener.onAddEstateTypeClicked(item.id)
                    }
                }
                
            } else {
                if (item.isSelected) {
                    if (isNightMode(binding.root.context)) {
                        //Selected night mode
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.primaryColorDark)
                        )
                    } else {
                        //selected Light mode
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.primaryColorLight)
                        )
                    }
                } else {
                    if (isNightMode(binding.root.context)) {
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.unselectButtonDark)
                        )
                    } else {
                        binding.estateTypeItemViewMaterialButtonButton.setBackgroundColor(
                            binding.root.context.getColor(R.color.unselectButtonLight)
                        )
                    }
                }
                
                binding.estateTypeItemViewMaterialButtonButton.setOnClickListener {
                    listener.onTypeClicked(item.id)
                }
            }
            
            
        }
    }
    
    object PointOfInterestDiffUtil : DiffUtil.ItemCallback<EstateTypeViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: EstateTypeViewStateItems,
            newItem: EstateTypeViewStateItems
        ): Boolean =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(
            oldItem: EstateTypeViewStateItems,
            newItem: EstateTypeViewStateItems
        ) = oldItem == newItem
    }
}