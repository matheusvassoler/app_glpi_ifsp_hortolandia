package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp

class TextViewBuilder(private val context: Context, text: String, width: Int, height: Int) {

    private var text: String = ""
    private var typeface: Typeface? = null
    private var textColor: Int? = null
    private var textSize: Float? = null
    private var width: Int = 0
    private var height: Int = 0
    private var leftMargin: Int? = null
    private var topMargin: Int? = null
    private var rightMargin: Int? = null
    private var bottomMargin: Int? = null
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var textView: TextView

    init {
        this.text = text
        this.width = width
        this.height = height
    }

    fun setTypeFace(typeface: Typeface) = apply {
        this.typeface = typeface
    }

    fun setTextColor(textColor: Int) = apply {
        this.textColor = textColor
    }

    fun setTextSize(textSize: Float) = apply {
        this.textSize = textSize
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

    fun build(): TextView {
        textView = TextView(context)
        textView.text = text

        setTextViewStyle()
        setTextViewDimensions()

        textView.layoutParams = layoutParams

        return textView
    }

    private fun setTextViewStyle() {
        textView.typeface = typeface
        setTextColorToTextView()
        setTextSizeToTextView()
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

    private fun setTextViewDimensions() {
        layoutParams = LinearLayout.LayoutParams(width, height)
        setLeftMarginToTextView()
        setTopMarginToTextView()
        setRightMarginToTextView()
        setBottomMarginToTextView()
    }

    private fun setLeftMarginToTextView() {
        leftMargin?.let {
            layoutParams.marginStart = it.toDp(context)
        }
    }

    private fun setTopMarginToTextView() {
        topMargin?.let {
            layoutParams.topMargin = it.toDp(context)
        }
    }

    private fun setRightMarginToTextView() {
        rightMargin?.let {
            layoutParams.marginEnd = it.toDp(context)
        }
    }

    private fun setBottomMarginToTextView() {
        bottomMargin?.let {
            layoutParams.bottomMargin = it.toDp(context)
        }
    }
}
