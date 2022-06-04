package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp

open class CheckBoxBuilder(private val context: Context) {

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
    private var rightIcon: Int? = null
    private var rightIconClickListener: View.OnClickListener? = null
    private var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    private var linearLayout: LinearLayout? = null
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

    fun setOnRightIconClickListener(listener: View.OnClickListener?) = apply {
        this.rightIconClickListener = listener
    }

    fun setRightIcon(icon: Int?) = apply {
        this.rightIcon = icon
    }

    fun build(): View {
        setCheckBoxDimensions()
        setCheckBox()

        return linearLayout ?: checkBox
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

        rightIcon?.let { icon ->
            linearLayout = LinearLayout(context)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayout?.orientation = LinearLayout.HORIZONTAL
            setLeftMarginToCheckBox()
            setTopMarginToCheckBox()
            setRightMarginToCheckBox()
            setBottomMarginToCheckBox()
            linearLayout?.layoutParams = layoutParams

            var lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9F)
            checkBox.layoutParams = lp
            linearLayout?.addView(checkBox)

            val imageView = ImageView(context)
            lp = LinearLayout.LayoutParams(0, 30.toDp(context), 0.1F)
            lp.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            imageView.setImageDrawable(ContextCompat.getDrawable(context, icon))
            imageView.layoutParams = lp

            rightIconClickListener?.let { listener ->
                imageView.setOnClickListener(listener)
            }

            linearLayout?.addView(imageView)
        }
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