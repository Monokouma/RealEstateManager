package com.despaircorp.ui.main.estate_form.point_of_interest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.PointOfInterestItemViewBinding
import com.despaircorp.ui.main.main_activity.utils.isNightMode


class PointOfInterestAdapter(
    private val listener: PointOfInterestListener
) : ListAdapter<PointOfInterestViewStateItems, PointOfInterestAdapter.PointOfInterestViewHolder>(
    PointOfInterestDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PointOfInterestViewHolder(
        PointOfInterestItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: PointOfInterestViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
    
    class PointOfInterestViewHolder(private val binding: PointOfInterestItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            item: PointOfInterestViewStateItems,
            listener: PointOfInterestListener,
        ) {
            binding.pointOfInterestItemViewMaterialButtonButton.text =
                binding.root.context.getString(item.pointOfInterestEnum.textRes)
            
            if (isNightMode(binding.root.context)) {
                if (item.isSelected) {
                    binding.pointOfInterestItemViewMaterialButtonButton.setBackgroundColor(
                        binding.root.context.getColor(R.color.primaryColorDark)
                    )
                } else {
                    binding.pointOfInterestItemViewMaterialButtonButton.setBackgroundColor(
                        binding.root.context.getColor(R.color.unselectButtonDark)
                    )
                }
            } else {
                if (item.isSelected) {
                    binding.pointOfInterestItemViewMaterialButtonButton.setBackgroundColor(
                        binding.root.context.getColor(R.color.primaryColorLight)
                    )
                } else {
                    binding.pointOfInterestItemViewMaterialButtonButton.setBackgroundColor(
                        binding.root.context.getColor(R.color.unselectButtonLight)
                    )
                }
            }
            
            binding.pointOfInterestItemViewMaterialButtonButton.setOnClickListener {
                if (item.isSelected) {
                    listener.onRemovePointOfInterestClicked(
                        item.id,
                    )
                } else {
                    listener.onAddPointOfInterestClicked(
                        item.id,
                    )
                }
                
            }
        }
    }
    
    object PointOfInterestDiffUtil : DiffUtil.ItemCallback<PointOfInterestViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: PointOfInterestViewStateItems,
            newItem: PointOfInterestViewStateItems
        ): Boolean =
            oldItem.pointOfInterestEnum.name == newItem.pointOfInterestEnum.name
        
        override fun areContentsTheSame(
            oldItem: PointOfInterestViewStateItems,
            newItem: PointOfInterestViewStateItems
        ) = oldItem == newItem
    }
}