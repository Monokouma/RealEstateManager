package com.despaircorp.ui.main.estate_form.picture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.despaircorp.ui.databinding.PictureItemsBinding
import com.despaircorp.ui.main.details_fragment.picture.EstatePictureListener


class EstateFormPictureAdapter(
    private val estatePictureListener: EstatePictureListener

) : ListAdapter<PictureViewStateItems, EstateFormPictureAdapter.EstateViewHolder>(
    EstatePictureDiffUtil
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EstateViewHolder(
        PictureItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    
    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        holder.bind(getItem(position), pictureListener = estatePictureListener)
    }
    
    class EstateViewHolder(private val binding: PictureItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            pictureViewStateItems: PictureViewStateItems,
            pictureListener: EstatePictureListener
        ) {
            binding.pictureItemsTextViewPictureType.text = pictureViewStateItems.type
            Glide.with(binding.pictureItemsImageViewPicture).load(pictureViewStateItems.bitmap)
                .into(binding.pictureItemsImageViewPicture)
            
            if (pictureViewStateItems.isInSelectionMode) {
                binding.pictureItemsImageViewDeleteIcon.isVisible = true
                binding.pictureItemsImageViewDeleteIcon.setOnClickListener {
                    pictureListener.onDeletePicture(pictureViewStateItems.id)
                }
            }
        }
    }
    
    object EstatePictureDiffUtil : DiffUtil.ItemCallback<PictureViewStateItems>() {
        override fun areItemsTheSame(
            oldItem: PictureViewStateItems,
            newItem: PictureViewStateItems
        ): Boolean =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(
            oldItem: PictureViewStateItems,
            newItem: PictureViewStateItems
        ) = oldItem == newItem
    }
}