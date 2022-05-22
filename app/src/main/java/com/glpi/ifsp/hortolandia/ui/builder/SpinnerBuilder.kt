package com.glpi.ifsp.hortolandia.ui.builder

import android.content.Context
import android.text.InputType
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp
import com.glpi.ifsp.hortolandia.ui.fragment.OpenTicketFormFragment
import com.google.android.material.textfield.TextInputLayout

class SpinnerBuilder(
    private val context: Context,
    private val values: ArrayList<String>,
    private val width: Int,
    private val height: Int
) {

    private var backgroundColor: Int? = null
    private var textSize: Float? = null
    private var hint: String? = null
    private var tag: Int? = null
    private var themeResId: Int? = null
    private var leftMargin: Int? = null
    private var topMargin: Int? = null
    private var rightMargin: Int? = null
    private var bottomMargin: Int? = null
    private var leftPadding: Int = 0
    private var topPadding: Int = 0
    private var rightPadding: Int = 0
    private var bottomPadding: Int = 0
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    fun setBackgroundColor(backgroundColor: Int) = apply {
        this.backgroundColor = backgroundColor
    }

    fun setTextSize(textSize: Float) = apply {
        this.textSize = textSize
    }

    fun setHint(hint: String) = apply {
        this.hint = hint
    }

    fun setTag(tag: Int) = apply {
        this.tag = tag
    }

    fun setThemeResId(themeResId: Int) = apply {
        this.themeResId = themeResId
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

    fun setLeftPadding(leftPadding: Int) = apply {
        this.leftPadding = leftPadding
    }

    fun setTopPadding(topPadding: Int) = apply {
        this.topPadding = topPadding
    }

    fun setRightPadding(rightPadding: Int) = apply {
        this.rightPadding = rightPadding
    }

    fun setBottomPadding(bottomPadding: Int) = apply {
        this.bottomPadding = bottomPadding
    }

    fun build(): Pair<TextInputLayout, AutoCompleteTextView> {
        setSpinnerDimensions()
        setTextInputLayout()
        setAutoCompleteTextView()
        textInputLayout.addView(autoCompleteTextView)

        return Pair(textInputLayout, autoCompleteTextView)
    }

    private fun setSpinnerDimensions() {
        layoutParams = LinearLayout.LayoutParams(width, height)
        setLeftMarginToSpinner()
        setTopMarginToSpinner()
        setRightMarginToSpinner()
        setBottomMarginToSpinner()
    }

    private fun setLeftMarginToSpinner() {
        leftMargin?.let {
            layoutParams.marginStart = it.toDp(context)
        }
    }

    private fun setTopMarginToSpinner() {
        topMargin?.let {
            layoutParams.topMargin = it.toDp(context)
        }
    }

    private fun setRightMarginToSpinner() {
        rightMargin?.let {
            layoutParams.marginEnd = it.toDp(context)
        }
    }

    private fun setBottomMarginToSpinner() {
        bottomMargin?.let {
            layoutParams.bottomMargin = it.toDp(context)
        }
    }

    private fun setTextInputLayout() {
        val newContext = getContextThemeWrapper(context, themeResId)
        textInputLayout = TextInputLayout(newContext)
        setBoxBackgroundColorToTextInputLayout()
        setHintToTextInputLayout()
        setTagToTextInputLayout()
    }

    private fun setBoxBackgroundColorToTextInputLayout() {
        backgroundColor?.let {
            textInputLayout.boxBackgroundColor = ContextCompat.getColor(context, it)
        }
    }

    private fun setHintToTextInputLayout() {
        hint?.let {
            textInputLayout.hint = it
        }
    }

    private fun setTagToTextInputLayout() {
        tag?.let {
            textInputLayout.tag = it
        }
    }

    private fun setAutoCompleteTextView() {
        autoCompleteTextView = AutoCompleteTextView(textInputLayout.context)
        val adapter: ArrayAdapter<String> = getAdapter()
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.inputType = InputType.TYPE_NULL
        autoCompleteTextView.layoutParams = layoutParams
        setTextSizeToAutoCompleteTextView()
        setPaddingToAutoCompleteTextView()
    }

    private fun getAdapter(): ArrayAdapter<String> {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.spinner_text_view,
            values
        )

        adapter.setDropDownViewResource(R.layout.dropdown_spinner_text_view)

        return adapter
    }

    private fun setTextSizeToAutoCompleteTextView() {
        textSize?.let {
            autoCompleteTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                it
            )
        }
    }

    private fun setPaddingToAutoCompleteTextView() {
        autoCompleteTextView.setPadding(
            leftPadding.toDp(context),
            topPadding.toDp(context),
            rightPadding.toDp(context),
            bottomPadding.toDp(context)
        )
    }

    private fun getContextThemeWrapper(context: Context, themeResId: Int?): ContextThemeWrapper {
        return themeResId?.let { ContextThemeWrapper(context, it) }
            ?: ContextThemeWrapper(
                context,
                R.style.Widget_MaterialComponents_TextInputLayout_FilledBox_ExposedDropdownMenu
            )
    }
}