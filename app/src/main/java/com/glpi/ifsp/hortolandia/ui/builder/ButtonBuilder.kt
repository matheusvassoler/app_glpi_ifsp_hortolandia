package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp

class ButtonBuilder(private val context: Context) {

    private var text: String = ""
    private var backgroundColor: Int? = null
    private var textColor: Int? = null
    private var textSize: Float? = null
    private var typeface: Typeface? = null
    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var leftMargin: Int? = null
    private var topMargin: Int? = null
    private var rightMargin: Int? = null
    private var bottomMargin: Int? = null
    private var radius: Float = 0.0F
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var button: CardView
    private lateinit var textView: TextView

    fun setText(text: String) = apply {
        this.text = text
    }

    fun setTypeFace(typeface: Typeface) = apply {
        this.typeface = typeface
    }

    fun setBackgroundColor(backgroundColor: Int) = apply {
        this.backgroundColor = backgroundColor
    }

    fun setTextColor(textColor: Int) = apply {
        this.textColor = textColor
    }

    fun setTextSize(textSize: Float) = apply {
        this.textSize = textSize
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

    fun setRadius(radius: Float) = apply {
        this.radius = radius
    }

    fun build(): CardView {
        setButtonDimensions()
        setButton()
        setTextView()
        button.addView(textView)
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
        button = CardView(context)
        button.layoutParams = layoutParams
        button.radius = radius
        setBackgroundColorToButton()
        setRippleEffectToButton()
    }

    private fun setBackgroundColorToButton() {
        backgroundColor?.let {
            button.setCardBackgroundColor(ContextCompat.getColor(context, it))
        }
        button.cardElevation = 0.0F
    }

    private fun setRippleEffectToButton() {
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val selectableItemBackground = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        button.foreground = ContextCompat.getDrawable(context, selectableItemBackground);
        button.isClickable = true;
    }

    private fun setTextView() {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        textView = TextView(context)
        textView.layoutParams = lp
        textView.text = text
        textView.gravity = Gravity.CENTER
        setTextColorToTextView()
        setTextSizeToTextView()
        setTypeFaceToTextView()
    }

    private fun setTextColorToTextView() {
        textColor?.let {
            textView.setTextColor(ContextCompat.getColor(context, it))
        }
    }

    private fun setTextSizeToTextView() {
        textSize?.let {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
        }
    }

    private fun setTypeFaceToTextView() {
        typeface?.let {
            textView.typeface = it
        }
    }
}