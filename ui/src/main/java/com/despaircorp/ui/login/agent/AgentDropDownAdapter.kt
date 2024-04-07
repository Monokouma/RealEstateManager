package com.despaircorp.ui.login.agent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.databinding.AgentDropDownItemBinding

class AgentDropDownAdapter(
    private val agentDropDownListener: AgentDropDownListener

) : ListAdapter<AgentDropDownViewStateItems, AgentDropDownAdapter.AgentDropDownViewHolder>(
    AgentDropDownDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AgentDropDownViewHolder(
        AgentDropDownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: AgentDropDownViewHolder, position: Int) {
        holder.bind(getItem(position), agentDropDownListener)
    }
    
    class AgentDropDownViewHolder(private val binding: AgentDropDownItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            agentDropDownViewStateItems: AgentDropDownViewStateItems,
            agentDropDownListener: AgentDropDownListener,
        ) {
            Glide.with(binding.agentDropDownItemShapeableImageUserImage)
                .load(agentDropDownViewStateItems.image)
                .into(binding.agentDropDownItemShapeableImageUserImage)
            binding.agentDropDownItemTextViewUsername.text = agentDropDownViewStateItems.name
            
            binding.root.setOnClickListener {
                agentDropDownListener.onAgentClick(agentDropDownViewStateItems.id)
            }
        }
    }
    
    object AgentDropDownDiffUtil : DiffUtil.ItemCallback<AgentDropDownViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: AgentDropDownViewStateItems,
            newItem: AgentDropDownViewStateItems
        ): Boolean =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(
            oldItem: AgentDropDownViewStateItems,
            newItem: AgentDropDownViewStateItems
        ) = oldItem == newItem
    }
}