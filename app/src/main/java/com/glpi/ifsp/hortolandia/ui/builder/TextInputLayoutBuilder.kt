package com.glpi.ifsp.hortolandia.ui.builder

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.glpi.ifsp.hortolandia.data.enums.FieldType
import com.glpi.ifsp.hortolandia.infrastructure.extensions.toDp
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class TextInputLayoutBuilder(private val context: Context) {

    private var backgroundColor: Int? = null
    private var textSize: Float? = null
    private var hint: String? = null
    private var tag: Int? = null
    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var leftMargin: Int? = null
    private var topMargin: Int? = null
    private var rightMargin: Int? = null
    private var bottomMargin: Int? = null
    private var fieldType: FieldType? = null
    private lateinit var layoutParams: LinearLayout.LayoutParams
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: TextInputEditText

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

    fun setWidth(width: Int) = apply {
        this.width = width
    }

    fun setHeight(height: Int) =  apply {
        this.height = height
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

    fun setFieldType(fieldType: FieldType) = apply {
        this.fieldType = fieldType
    }

    fun build(): TextInputLayout {
        setTextInputLayoutDimensions()
        setTextInputLayout()
        setInputTypeToTextInputEditText()
        textInputLayout.addView(textInputEditText)
        return textInputLayout
    }

    private fun setTextInputLayoutDimensions() {
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
        textInputLayout = TextInputLayout(context)
        textInputLayout.layoutParams = layoutParams
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

    private fun setInputTypeToTextInputEditText() {
        textInputEditText = TextInputEditText(textInputLayout.context)
        when (fieldType) {
            FieldType.DATE -> textInputEditText.transformIntoDatePicker(
                context,
                BRAZILIAN_DATE_FORMAT,
                Date()
            )
            FieldType.INTEGER -> textInputEditText.inputType = InputType.TYPE_CLASS_NUMBER
            else -> textInputEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }

    private fun TextInputEditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false
        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener = onDateSetListener(myCalendar, format)
        setOnClickListener(context, datePickerOnDataSetListener, myCalendar, maxDate)
    }

    private fun TextInputEditText.onDateSetListener(
        myCalendar: Calendar,
        format: String
    ) = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val sdf = SimpleDateFormat(format, Locale.UK)
        setText(sdf.format(myCalendar.time))
    }

    private fun TextInputEditText.setOnClickListener(
        context: Context,
        datePickerOnDataSetListener: DatePickerDialog.OnDateSetListener,
        myCalendar: Calendar,
        maxDate: Date?
    ) {
        setOnClickListener {
            it?.clearFocus()
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    companion object {
        private const val BRAZILIAN_DATE_FORMAT = "dd/MM/yyyy"
    }
}