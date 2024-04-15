package com.despaircorp.ui.main.estate_addition.agent

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.AgentAdditionItemBinding
import com.despaircorp.ui.utils.isNightMode

class CreateEstateAgentAdapter(
    private val agentDropDownListener: CreateEstateAgentListener

) : ListAdapter<CreateEstateAgentViewStateItems, CreateEstateAgentAdapter.AgentDropDownViewHolder>(
    AgentDropDownDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AgentDropDownViewHolder(
        AgentAdditionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: AgentDropDownViewHolder, position: Int) {
        holder.bind(getItem(position), agentDropDownListener)
    }
    
    class AgentDropDownViewHolder(private val binding: AgentAdditionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            agentDropDownViewStateItems: CreateEstateAgentViewStateItems,
            agentDropDownListener: CreateEstateAgentListener,
        ) {
            Glide.with(binding.agentDropDownItemShapeableImageUserImage)
                .load(agentDropDownViewStateItems.image)
                .into(binding.agentDropDownItemShapeableImageUserImage)
            binding.agentDropDownItemTextViewUsername.text = agentDropDownViewStateItems.name
            
            if (agentDropDownViewStateItems.isSelected) {
                if (isNightMode(binding.root.context)) {
                    binding.agentDropDownItemCardviewRoot.setCardBackgroundColor(
                        ColorStateList.valueOf(
                            binding.root.context.getColor(
                                R.color.primaryColorDark
                            )
                        )
                    )
                } else {
                    binding.agentDropDownItemCardviewRoot.setCardBackgroundColor(
                        ColorStateList.valueOf(
                            binding.root.context.getColor(
                                R.color.primaryColorLight
                            )
                        )
                    )
                }
                
            } else {
                if (isNightMode(binding.root.context)) {
                    binding.agentDropDownItemCardviewRoot.setCardBackgroundColor(
                        ColorStateList.valueOf(
                            binding.root.context.getColor(
                                R.color.backgroundColorDark
                            )
                        )
                    )
                } else {
                    binding.agentDropDownItemCardviewRoot.setCardBackgroundColor(
                        ColorStateList.valueOf(
                            binding.root.context.getColor(
                                R.color.backgroundColorLight
                            )
                        )
                    )
                }
                
            }
            
            binding.agentDropDownItemCardviewRoot.setOnClickListener {
                
                
                agentDropDownListener.onAgentClick(agentDropDownViewStateItems.id)
            }
        }
    }
    
    object AgentDropDownDiffUtil : DiffUtil.ItemCallback<CreateEstateAgentViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: CreateEstateAgentViewStateItems,
            newItem: CreateEstateAgentViewStateItems
        ): Boolean =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(
            oldItem: CreateEstateAgentViewStateItems,
            newItem: CreateEstateAgentViewStateItems
        ) = oldItem == newItem
    }
}