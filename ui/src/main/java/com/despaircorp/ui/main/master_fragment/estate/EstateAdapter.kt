package com.despaircorp.ui.main.master_fragment.estate

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.EstateItemsViewBinding
import com.despaircorp.ui.main.main_activity.utils.isNightMode

class EstateAdapter(
    private val estateListener: EstateListener

) : ListAdapter<EstateViewStateItems, EstateAdapter.EstateViewHolder>(
    EstateDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EstateViewHolder(
        EstateItemsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        holder.bind(getItem(position), estateListener)
    }
    
    class EstateViewHolder(private val binding: EstateItemsViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            estateViewStateItems: EstateViewStateItems,
            estateListener: EstateListener,
        ) {
            Glide.with(binding.estateItemsViewImageViewEstatePicture)
                .load(estateViewStateItems.picture)
                .into(binding.estateItemsViewImageViewEstatePicture)
            
            binding.estateItemsViewTextViewEstateCity.text = estateViewStateItems.city
            binding.estateItemsViewTextViewEstatePrice.text = estateViewStateItems.price
            binding.estateItemsViewTextViewEstateType.text = estateViewStateItems.type
            
            
            
            if (estateViewStateItems.isSelected) {
                if (isNightMode(binding.root.context)) {
                    binding.estateItemsViewTextViewEstatePrice.setTextColor(
                        binding.estateItemsViewTextViewEstatePrice.context.getColor(
                            R.color.backgroundColorDark
                        )
                    )
                    binding.estateItemsViewCardRoot.setCardBackgroundColor(
                        ColorStateList.valueOf(
                            binding.root.context.getColor(
                                R.color.accentColorDark
                            )
                        )
                    )
                    
                } else {
                    binding.estateItemsViewTextViewEstatePrice.setTextColor(
                        binding.estateItemsViewTextViewEstatePrice.context.getColor(
                            R.color.backgroundColorLight
                        )
                    )
                    binding.estateItemsViewCardRoot.setCardBackgroundColor(
                        binding.root.context.getColor(
                            R.color.accentColorLight
                        )
                    )
                }
                
            } else {
                if (isNightMode(binding.root.context)) {
                    binding.estateItemsViewTextViewEstatePrice.setTextColor(
                        binding.estateItemsViewTextViewEstatePrice.context.getColor(
                            R.color.accentColorDark
                        )
                    )
                    binding.estateItemsViewCardRoot.setCardBackgroundColor(
                        binding.root.context.getColor(
                            R.color.backgroundColorDark
                        )
                    )
                } else {
                    binding.estateItemsViewTextViewEstatePrice.setTextColor(
                        binding.estateItemsViewTextViewEstatePrice.context.getColor(
                            R.color.accentColorLight
                        )
                    )
                    binding.estateItemsViewCardRoot.setCardBackgroundColor(
                        binding.root.context.getColor(
                            R.color.backgroundColorLight
                        )
                    )
                }
            }
            
            binding.root.setOnClickListener {
                estateListener.onEstateClicked(estateViewStateItems.id)
            }
        }
    }
    
    object EstateDiffUtil : DiffUtil.ItemCallback<EstateViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: EstateViewStateItems,
            newItem: EstateViewStateItems
        ): Boolean =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(
            oldItem: EstateViewStateItems,
            newItem: EstateViewStateItems
        ) = oldItem == newItem
    }
}