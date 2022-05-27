package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp

class SpaceBuilder(private val context: Context) {

    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var weight: Float? = null
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var space: Space

    fun setWidth(width: Int) = apply {
        if (width == ViewGroup.LayoutParams.MATCH_PARENT ||
            width == ViewGroup.LayoutParams.WRAP_CONTENT
        ) {
            this.width = width
        } else {
            this.width = width.toDp(context)
        }
    }

    fun setHeight(height: Int) = apply {
        if (height == ViewGroup.LayoutParams.MATCH_PARENT ||
            height == ViewGroup.LayoutParams.WRAP_CONTENT
        ) {
            this.height = height
        } else {
            this.height = height.toDp(context)
        }
    }

    fun setWeight(weight: Float) = apply {
        this.weight = weight
    }

    fun build(): Space {
        setSpaceDimensions()
        space = Space(context)
        space.layoutParams = layoutParams
        return space
    }

    private fun setSpaceDimensions() {
        layoutParams = weight?.let {
            LinearLayout.LayoutParams(width, height, it)
        } ?: LinearLayout.LayoutParams(width, height)
    }
}