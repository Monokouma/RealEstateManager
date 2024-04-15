package com.despaircorp.ui.main.details_fragment.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.despaircorp.ui.R

class EstateDetailsSquareInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var titleTextView: TextView
    var iconImageView: ImageView
    var textTextView: TextView
    
    init {
        // Inflate the custom layout
        inflate(context, R.layout.estate_details_square_info_view, this)
        
        // Reference to the views
        titleTextView = findViewById(R.id.estate_details_square_info_view_TextView_surface_title)
        iconImageView = findViewById(R.id.estate_details_square_info_view_ImageView_size_icon)
        textTextView = findViewById(R.id.estate_details_square_info_view_TextView_surface)
        
        // Load attributes and set them
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EstateDetailsSquareInfoView,
            0, 0
        ).apply {
            try {
                titleTextView.text = getString(R.styleable.EstateDetailsSquareInfoView_customTitle)
                textTextView.text = getString(R.styleable.EstateDetailsSquareInfoView_customText)
                iconImageView.setImageDrawable(getDrawable(R.styleable.EstateDetailsSquareInfoView_customIcon))
            } finally {
                recycle()
            }
        }
    }
}