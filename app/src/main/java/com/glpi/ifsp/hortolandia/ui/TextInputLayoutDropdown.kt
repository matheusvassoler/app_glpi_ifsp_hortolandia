package com.glpi.ifsp.hortolandia.ui

import android.content.Context
import android.util.AttributeSet
import com.glpi.ifsp.hortolandia.R
import com.google.android.material.textfield.TextInputLayout

class TextInputLayoutDropdown @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_MaterialComponents_TextInputLayout_FilledBox_ExposedDropdownMenu,
) : TextInputLayout(context, attributeSet, defStyleAttr)