package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp

class ButtonBuilder(private val context: Context) {

    private var text: String = ""
    private var fontStyle: Int = Typeface.NORMAL
    private var backgroundColor: Int? = null
    private var textColor: Int? = null
    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var leftMargin: Int? = null
    private var topMargin: Int? = null
    private var rightMargin: Int? = null
    private var bottomMargin: Int? = null
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var button: Button

    fun setText(text: String) = apply {
        this.text = text
    }

    fun setFontStyle(fontStyle: Int) = apply {
        this.fontStyle = fontStyle
    }

    fun setBackgroundColor(backgroundColor: Int) = apply {
        this.backgroundColor = backgroundColor
    }

    fun setTextColor(textColor: Int) = apply {
        this.textColor = textColor
    }

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

    fun setLeftMargin(leftMargin: Int) = apply {
        this.leftMargin = leftMargin
    }

    fun setTopMargin(topMargin: Int) = apply {
        this.topMargin = topMargin
    }

    fun setRightMargin(rightMargin: Int) = apply {
        this.rightMargin = rightMargin
    }

    fun setBottomMargin(bottomMargin: Int) = apply {
        this.bottomMargin = bottomMargin
    }

    fun build(): Button {
        setButtonDimensions()
        setButton()
        return button
    }

    private fun setButtonDimensions() {
        layoutParams = LinearLayout.LayoutParams(width, height)
        setLeftMarginToTextInputLayout()
        setTopMarginToTextInputLayout()
        setRightMarginToTextInputLayout()
        setBottomMarginToTextInputLayout()
    }

    private fun setLeftMarginToTextInputLayout() {
        leftMargin?.let {
            layoutParams.marginStart = it.toDp(context)
        }
    }

    private fun setTopMarginToTextInputLayout() {
        topMargin?.let {
            layoutParams.topMargin = it.toDp(context)
        }
    }

    private fun setRightMarginToTextInputLayout() {
        rightMargin?.let {
            layoutParams.marginEnd = it.toDp(context)
        }
    }

    private fun setBottomMarginToTextInputLayout() {
        bottomMargin?.let {
            layoutParams.bottomMargin = it.toDp(context)
        }
    }

    private fun setButton() {
        button = Button(context)
        button.layoutParams = layoutParams
        button.setTypeface(null, fontStyle)
        button.text = text
        setBackgroundColorToButton()
        setTextColorToButton()
    }

    private fun setBackgroundColorToButton() {
        backgroundColor?.let {
            button.setBackgroundColor(ContextCompat.getColor(context, it))
        }
    }

    private fun setTextColorToButton() {
        textColor?.let {
            button.setTextColor(ContextCompat.getColor(context, it))
        }
    }
}