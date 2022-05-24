package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp

class CheckBoxBuilder(private val context: Context) {

    private var text: String = ""
    private var tag: Int? = null
    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var textSize: Float? = null
    private var textColor: Int? = null
    private var leftMargin: Int? = null
    private var topMargin: Int? = null
    private var rightMargin: Int? = null
    private var bottomMargin: Int? = null
    private var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var checkBox: CheckBox

    fun setText(text: String) = apply {
        this.text = text
    }

    fun setTag(tag: Int) = apply {
        this.tag = tag
    }

    fun setWidth(width: Int) = apply {
        this.width = width
    }

    fun setHeight(height: Int) = apply {
        this.height = height
    }

    fun setTextSize(textSize: Float) = apply {
        this.textSize = textSize
    }

    fun setTextColor(textColor: Int) = apply {
        this.textColor = textColor
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

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener?) = apply {
        this.onCheckedChangeListener = listener
    }

    fun build(): CheckBox {
        setCheckBoxDimensions()
        setCheckBox()
        return checkBox
    }

    private fun setCheckBoxDimensions() {
        layoutParams = LinearLayout.LayoutParams(width, height)
        setLeftMarginToCheckBox()
        setTopMarginToCheckBox()
        setRightMarginToCheckBox()
        setBottomMarginToCheckBox()
    }

    private fun setLeftMarginToCheckBox() {
        leftMargin?.let {
            layoutParams.marginStart = it.toDp(context)
        }
    }

    private fun setTopMarginToCheckBox() {
        topMargin?.let {
            layoutParams.topMargin = it.toDp(context)
        }
    }

    private fun setRightMarginToCheckBox() {
        rightMargin?.let {
            layoutParams.marginEnd = it.toDp(context)
        }
    }

    private fun setBottomMarginToCheckBox() {
        bottomMargin?.let {
            layoutParams.bottomMargin = it.toDp(context)
        }
    }

    private fun setCheckBox() {
        checkBox = CheckBox(context)
        checkBox.layoutParams = layoutParams
        checkBox.text = text
        setTagToCheckBox()
        setTextColorToCheckBox()
        setTextSizeToCheckBox()
        setOnCheckedChangeListenerToCheckBox()
    }

    private fun setTagToCheckBox() {
        tag?.let {
            checkBox.tag = it
        }
    }

    private fun setTextColorToCheckBox() {
        textColor?.let {
            checkBox.setTextColor(ContextCompat.getColor(context, it))
        }
    }

    private fun setTextSizeToCheckBox() {
        textSize?.let {
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
        }
    }

    private fun setOnCheckedChangeListenerToCheckBox() {
        onCheckedChangeListener?.let {
            checkBox.setOnCheckedChangeListener(it)
        }
    }
}