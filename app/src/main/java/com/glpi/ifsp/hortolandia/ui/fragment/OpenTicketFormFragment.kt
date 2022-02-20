package com.glpi.ifsp.hortolandia.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.data.enums.FieldRule
import com.glpi.ifsp.hortolandia.data.enums.FieldType
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketFormBinding
import com.glpi.ifsp.hortolandia.ui.activity.FailedRegistrationActivity
import com.glpi.ifsp.hortolandia.ui.activity.LoginActivity
import com.glpi.ifsp.hortolandia.ui.activity.RegistrationSuccessfullyActivity
import com.glpi.ifsp.hortolandia.ui.activity.RequestErrorActivity
import com.glpi.ifsp.hortolandia.ui.event.OpenTicketEvent
import com.glpi.ifsp.hortolandia.ui.model.FormUI
import com.glpi.ifsp.hortolandia.ui.model.QuestionUI
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import com.glpi.ifsp.hortolandia.ui.viewmodel.OpenTicketViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.HashMap
import org.koin.android.viewmodel.ext.android.viewModel

private const val FORM_URL_PARAM = "FORM_URL_PARAM"
private const val ID = "id"

@Suppress("TooManyFunctions", "LongMethod", "MaxLineLength", "ComplexMethod", "ComplexCondition", "LargeClass")
class OpenTicketFormFragment : Fragment() {

    private val openTicketViewModel: OpenTicketViewModel by viewModel()
    private var _binding: FragmentOpenTicketFormBinding? = null
    private val binding get() = _binding!!

    private var formUrlParam: String = ""
    private var formId: Int? = null

    private var title: String = ""
    private var description: String = ""

    private lateinit var layoutParams: LinearLayout.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            formUrlParam = it.getString(FORM_URL_PARAM, "")
        }

        val uri = Uri.parse(formUrlParam)
        formId = uri.getQueryParameter(ID)?.toInt()

        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.topMargin = TWENTY_MARGIN_TOP.toDp(requireContext())
        layoutParams.marginStart = TWENTY_MARGIN_START.toDp(requireContext())
        layoutParams.marginEnd = TWENTY_MARGIN_END.toDp(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenTicketFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openTicketViewModel.onStart(formId)
        binding.fragmentOpenTicketFormToolbar.toolbarTitle.text =
            getString(R.string.open_ticket_header_title)

        openTicketViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OpenTicketState.ShowLoading -> {
                    binding.fragmentOpenTicketFormProgressBar.visibility = View.VISIBLE
                }
                is OpenTicketState.ShowResponseRequestError -> {
                    startActivity(RequestErrorActivity.newInstance(requireContext()))
                }
                is OpenTicketState.ShowFormUI -> {
                    binding.fragmentOpenTicketFormProgressBar.visibility = View.INVISIBLE

                    val textView = TextView(requireContext())
                    textView.text = it.formUI.name
                    textView.typeface = Typeface.DEFAULT_BOLD
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_18_SP)

                    val layoutParamsForFormTitle = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    layoutParamsForFormTitle.topMargin = THIRTY_MARGIN_TOP.toDp(requireContext())
                    layoutParamsForFormTitle.marginStart = TWENTY_MARGIN_START.toDp(requireContext())
                    layoutParamsForFormTitle.marginEnd = TWENTY_MARGIN_END.toDp(requireContext())
                    textView.layoutParams = layoutParamsForFormTitle

                    binding.fragmentOpenTicketFormLayout.addView(textView)

                    it.formUI.questions.forEach { question ->
                        when (question.fieldType) {
                            FieldType.TEXT -> {
                                val til = setupTextInputLayout(question)
                                binding.fragmentOpenTicketFormLayout.addView(til)
                            }
                            FieldType.DATE -> {
                                val til = setupTextInputLayout(question)
                                binding.fragmentOpenTicketFormLayout.addView(til)
                            }
                            FieldType.EMAIL -> {
                                val til = setupTextInputLayout(question)
                                binding.fragmentOpenTicketFormLayout.addView(til)
                            }
                            FieldType.SELECT -> {
                                val conditionsToHideOrShowQuestions =
                                    it.formUI.conditionsToHideOrShowQuestions

                                val optionsToSelect =
                                    question.values?.split("\r\n")?.filter { item ->
                                        item != ""
                                    } as ArrayList<String>

                                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                    requireContext(),
                                    R.layout.spinner_text_view,
                                    optionsToSelect
                                )

                                adapter.setDropDownViewResource(R.layout.dropdown_spinner_text_view)
                                var selectedItem = ""

                                val (textInputLayout, autoCompleteTextView) = setupTextInputLayoutForSpinner(adapter, question)

                                if (it.formUI.conditionsToHideOrShowQuestions.isNotEmpty()) {
                                    autoCompleteTextView.onItemClickListener =
                                        AdapterView.OnItemClickListener { _, view, _, _ ->
                                            val selectedOption = (view as TextView).text.toString()

                                            if (selectedItem != "") {
                                                conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                    if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedItem) {
                                                        val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                        for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                            val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                            if (field.tag == idOfQuestionShouldAppearOrDisappear || field.tag == "text_field_$idOfQuestionShouldAppearOrDisappear" || field.tag == "spinner_$idOfQuestionShouldAppearOrDisappear" || field.tag == "checkbox_$idOfQuestionShouldAppearOrDisappear") {
                                                                field.visibility = View.GONE
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedOption) {
                                                    val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                    for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                        val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                        if (field.tag == idOfQuestionShouldAppearOrDisappear || field.tag == "text_field_$idOfQuestionShouldAppearOrDisappear" || field.tag == "spinner_$idOfQuestionShouldAppearOrDisappear" || field.tag == "checkbox_$idOfQuestionShouldAppearOrDisappear") {
                                                            val questionShouldAppearOrDisappear = it.formUI.questions.find { questionUI ->
                                                                questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                            }
                                                            val ruleType = questionShouldAppearOrDisappear?.fieldRule

                                                            if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                field.visibility = View.VISIBLE
                                                            } else {
                                                                field.visibility = View.GONE
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (selectedItem != selectedOption) {
                                                selectedItem = selectedOption
                                            }
                                        }
                                }

                                binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
                            }
                            FieldType.CHECKBOXES -> {
                                if (question.name.lowercase().contains("termos e condições")) {
                                    title = question.name
                                    description = question.description
                                    return@forEach
                                }

                                val conditionsToHideOrShowQuestions =
                                    it.formUI.conditionsToHideOrShowQuestions

                                if (question.values?.contains("\r\n") == true) {
                                    val splittedText = question.values.split("\r\n")

                                    splittedText.forEach { questionInfo ->
                                        val checkBox = CheckBox(requireContext())
                                        checkBox.text = questionInfo

                                        checkBox.id = question.id
                                        checkBox.tag = "checkbox_" + question.id

                                        if (question.fieldRule == FieldRule.HIDDEN_UNLESS) {
                                            checkBox.visibility = View.GONE
                                        }

                                        checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                                            val optionSelected = questionInfo

                                            conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                if (ruleToShowQuestionUI.valueThatTriggersCondition == optionSelected) {
                                                    val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                    for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                        val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)

                                                        if (field.tag == idOfQuestionShouldAppearOrDisappear || field.tag == "text_field_$idOfQuestionShouldAppearOrDisappear" || field.tag == "spinner_$idOfQuestionShouldAppearOrDisappear") {
                                                            val questionShouldAppearOrDisappear = it.formUI.questions.find { questionUI ->
                                                                questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                            }
                                                            val ruleType = questionShouldAppearOrDisappear?.fieldRule

                                                            if (isChecked) {
                                                                if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                    field.visibility = View.VISIBLE
                                                                } else {
                                                                    field.visibility = View.GONE
                                                                }
                                                            } else {
                                                                if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                    field.visibility = View.GONE
                                                                } else {
                                                                    field.visibility = View.VISIBLE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        val layoutParamsForCheckbox = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )

                                        layoutParamsForCheckbox.topMargin = TWENTY_MARGIN_TOP.toDp(requireContext())
                                        layoutParamsForCheckbox.marginStart = FOURTEEN_MARGIN_START.toDp(requireContext())
                                        layoutParamsForCheckbox.marginEnd = FOURTEEN_MARGIN_END.toDp(requireContext())
                                        checkBox.layoutParams = layoutParamsForCheckbox
                                        checkBox.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_dark_for_field_hint))
                                        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_16_SP)
                                        binding.fragmentOpenTicketFormLayout.addView(checkBox)
                                    }
                                } else {
                                    val checkBox = CheckBox(requireContext())
                                    checkBox.text = question.values

                                    checkBox.id = question.id
                                    checkBox.tag = "checkbox_" + question.id

                                    if (question.fieldRule == FieldRule.HIDDEN_UNLESS) {
                                        checkBox.visibility = View.GONE
                                    }

                                    checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                                        val optionSelected = question.values

                                        conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                            if (ruleToShowQuestionUI.valueThatTriggersCondition == optionSelected) {
                                                val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                    val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)

                                                    if (field.tag == idOfQuestionShouldAppearOrDisappear || field.tag == "text_field_$idOfQuestionShouldAppearOrDisappear" || field.tag == "spinner_$idOfQuestionShouldAppearOrDisappear") {
                                                        val questionShouldAppearOrDisappear = it.formUI.questions.find { questionUI ->
                                                            questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                        }
                                                        val ruleType = questionShouldAppearOrDisappear?.fieldRule

                                                        if (isChecked) {
                                                            if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                field.visibility = View.VISIBLE
                                                            } else {
                                                                field.visibility = View.GONE
                                                            }
                                                        } else {
                                                            if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                field.visibility = View.GONE
                                                            } else {
                                                                field.visibility = View.VISIBLE
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    val layoutParamsForCheckbox = LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )

                                    layoutParamsForCheckbox.topMargin = FORTY_MARGIN_TOP.toDp(requireContext())
                                    layoutParamsForCheckbox.marginStart = FOURTEEN_MARGIN_START.toDp(requireContext())
                                    layoutParamsForCheckbox.marginEnd = FOURTEEN_MARGIN_END.toDp(requireContext())
                                    checkBox.layoutParams = layoutParamsForCheckbox
                                    checkBox.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_dark_for_field_hint))
                                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_16_SP)
                                    binding.fragmentOpenTicketFormLayout.addView(checkBox)
                                }
                            }
                            FieldType.INTEGER -> {
                                val til = setupTextInputLayout(question)
                                binding.fragmentOpenTicketFormLayout.addView(til)
                            }
                            FieldType.GLPISELECT -> {
                                if (question.items != null) {
                                    val conditionsToHideOrShowQuestions =
                                        it.formUI.conditionsToHideOrShowQuestions

                                    val optionsToSelect = question.items.map { item ->
                                        item.name
                                    } as ArrayList<String>

                                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                        requireContext(),
                                        R.layout.spinner_text_view,
                                        optionsToSelect
                                    )

                                    adapter.setDropDownViewResource(R.layout.dropdown_spinner_text_view)
                                    var selectedItem = ""

                                    val (textInputLayout, autoCompleteTextView) = setupTextInputLayoutForSpinner(adapter, question)
                                    if (it.formUI.conditionsToHideOrShowQuestions.isNotEmpty()) {
                                        autoCompleteTextView.onItemClickListener =
                                            AdapterView.OnItemClickListener { _, view, _, _ ->
                                                val selectedOption =
                                                    (view as TextView).text.toString()

                                                if (selectedItem != "") {
                                                    conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                        if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedItem) {
                                                            val idOfQuestionShouldAppearOrDisappear =
                                                                ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                            for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                                val field =
                                                                    binding.fragmentOpenTicketFormLayout.getChildAt(
                                                                        i
                                                                    )
                                                                if (field.tag == idOfQuestionShouldAppearOrDisappear) {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                    if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedOption) {
                                                        val idOfQuestionShouldAppearOrDisappear =
                                                            ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                        for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                            val field =
                                                                binding.fragmentOpenTicketFormLayout.getChildAt(
                                                                    i
                                                                )
                                                            if (field.tag == idOfQuestionShouldAppearOrDisappear || field.tag == "text_field_$idOfQuestionShouldAppearOrDisappear" || field.tag == "spinner_$idOfQuestionShouldAppearOrDisappear" || field.tag == "checkbox_$idOfQuestionShouldAppearOrDisappear") {
                                                                val questionShouldAppearOrDisappear =
                                                                    it.formUI.questions.find { questionUI ->
                                                                        questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                                    }
                                                                val ruleType =
                                                                    questionShouldAppearOrDisappear?.fieldRule

                                                                if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                    field.visibility = View.VISIBLE
                                                                } else {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                if (selectedItem != selectedOption) {
                                                    selectedItem = selectedOption
                                                }
                                            }
                                    }

                                    binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
                                } else if (question.locations != null) {
                                    val conditionsToHideOrShowQuestions =
                                        it.formUI.conditionsToHideOrShowQuestions

                                    val optionsToSelect = question.locations.map { item ->
                                        item.name
                                    } as ArrayList<String>

                                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                        requireContext(),
                                        R.layout.spinner_text_view,
                                        optionsToSelect
                                    )

                                    adapter.setDropDownViewResource(R.layout.dropdown_spinner_text_view)
                                    var selectedItem = ""

                                    val (textInputLayout, autoCompleteTextView) = setupTextInputLayoutForSpinner(adapter, question)

                                    if (it.formUI.conditionsToHideOrShowQuestions.isNotEmpty()) {
                                        autoCompleteTextView.onItemClickListener =
                                            AdapterView.OnItemClickListener { _, view, _, _ ->
                                                val selectedOption = (view as TextView).text.toString()

                                                if (selectedItem != "") {
                                                    conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                        if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedItem) {
                                                            val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                            for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                                val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                                if (field.tag == idOfQuestionShouldAppearOrDisappear) {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                    if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedOption) {
                                                        val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                        for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                            val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                            if (field.tag == idOfQuestionShouldAppearOrDisappear) {
                                                                val questionShouldAppearOrDisappear = it.formUI.questions.find { questionUI ->
                                                                    questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                                }
                                                                val ruleType = questionShouldAppearOrDisappear?.fieldRule

                                                                if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                    field.visibility = View.VISIBLE
                                                                } else {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                if (selectedItem != selectedOption) {
                                                    selectedItem = selectedOption
                                                }
                                            }
                                    }

                                    binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
                                }
                            }
                            FieldType.DROPDOWN -> {
                                if (question.items != null) {
                                    val conditionsToHideOrShowQuestions =
                                        it.formUI.conditionsToHideOrShowQuestions

                                    val optionsToSelect = question.items.map { item ->
                                        item.name
                                    } as ArrayList<String>

                                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                        requireContext(),
                                        R.layout.spinner_text_view,
                                        optionsToSelect
                                    )

                                    adapter.setDropDownViewResource(R.layout.dropdown_spinner_text_view)
                                    var selectedItem = ""

                                    val (textInputLayout, autoCompleteTextView) = setupTextInputLayoutForSpinner(adapter, question)

                                    if (it.formUI.conditionsToHideOrShowQuestions.isNotEmpty()) {
                                        autoCompleteTextView.onItemClickListener =
                                            AdapterView.OnItemClickListener { _, view, _, _ ->
                                                val selectedOption = (view as TextView).text.toString()

                                                if (selectedItem != "") {
                                                    conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                        if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedItem) {
                                                            val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                            for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                                val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                                if (field.tag == idOfQuestionShouldAppearOrDisappear) {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                    if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedOption) {
                                                        val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                        for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                            val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                            if (field.tag == idOfQuestionShouldAppearOrDisappear || field.tag == "text_field_$idOfQuestionShouldAppearOrDisappear" || field.tag == "spinner_$idOfQuestionShouldAppearOrDisappear" || field.tag == "checkbox_$idOfQuestionShouldAppearOrDisappear") {
                                                                val questionShouldAppearOrDisappear = it.formUI.questions.find { questionUI ->
                                                                    questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                                }
                                                                val ruleType = questionShouldAppearOrDisappear?.fieldRule

                                                                if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                    field.visibility = View.VISIBLE
                                                                } else {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                if (selectedItem != selectedOption) {
                                                    selectedItem = selectedOption
                                                }
                                            }
                                    }

                                    binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
                                } else if (question.locations != null) {
                                    val conditionsToHideOrShowQuestions =
                                        it.formUI.conditionsToHideOrShowQuestions

                                    val optionsToSelect = question.locations.map { item ->
                                        item.name
                                    } as ArrayList<String>

                                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                        requireContext(),
                                        R.layout.spinner_text_view,
                                        optionsToSelect
                                    )

                                    adapter.setDropDownViewResource(R.layout.dropdown_spinner_text_view)
                                    var selectedItem = ""

                                    val (textInputLayout, autoCompleteTextView) = setupTextInputLayoutForSpinner(adapter, question)

                                    if (it.formUI.conditionsToHideOrShowQuestions.isNotEmpty()) {
                                        autoCompleteTextView.onItemClickListener =
                                            AdapterView.OnItemClickListener { _, view, _, _ ->
                                                val selectedOption = (view as TextView).text.toString()

                                                if (selectedItem != "") {
                                                    conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                        if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedItem) {
                                                            val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                            for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                                val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                                if (field.tag == idOfQuestionShouldAppearOrDisappear) {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                conditionsToHideOrShowQuestions.forEach { ruleToShowQuestionUI ->
                                                    if (ruleToShowQuestionUI.valueThatTriggersCondition == selectedOption) {
                                                        val idOfQuestionShouldAppearOrDisappear = ruleToShowQuestionUI.questionIdThatDisappearsOrAppearsBasedOnACondition

                                                        for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                                            val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)
                                                            if (field.tag == idOfQuestionShouldAppearOrDisappear) {
                                                                val questionShouldAppearOrDisappear = it.formUI.questions.find { questionUI ->
                                                                    questionUI.id == idOfQuestionShouldAppearOrDisappear
                                                                }
                                                                val ruleType = questionShouldAppearOrDisappear?.fieldRule

                                                                if (ruleType == FieldRule.HIDDEN_UNLESS) {
                                                                    field.visibility = View.VISIBLE
                                                                } else {
                                                                    field.visibility = View.GONE
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                if (selectedItem != selectedOption) {
                                                    selectedItem = selectedOption
                                                }
                                            }
                                    }

                                    binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
                                }
                            }
                            FieldType.TEXTAREA -> {
                                val til = TextInputLayout(requireContext())
                                til.layoutParams = layoutParams
                                til.hint = question.name
                                til.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.white)
                                val et =
                                    TextInputEditText(til.context) // important: get the themed context from the TextInputLayout

                                et.inputType = InputType.TYPE_CLASS_TEXT or
                                        InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                til.addView(et)

                                til.id = question.id
                                til.tag = question.id

                                if (question.fieldRule == FieldRule.HIDDEN_UNLESS) {
                                    til.visibility = View.GONE
                                }

                                binding.fragmentOpenTicketFormLayout.addView(til)
                            }
                        }
                    }

                    setupButton(it.formUI)
                }
            }
        })

        openTicketViewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OpenTicketEvent.OpenLoginScreen -> {
                    startActivity(LoginActivity.newInstance(requireContext()))
                }
                is OpenTicketEvent.OpenFailedRegistrationScreen -> {
                    startActivity(
                        FailedRegistrationActivity.newInstance(
                            requireContext(),
                            it.ticketTitle,
                            it.answersToSave
                        )
                    )
                }
                is OpenTicketEvent.OpenRegistrationScreenSuccessfully -> {
                    startActivity(RegistrationSuccessfullyActivity.newInstance(requireContext()))
                }
            }
        })
    }

    private fun setupButton(formUI: FormUI) {
        val layoutParamsForButton = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            FIFTY_HEIGHT.toDp(requireContext())
        )

        layoutParamsForButton.topMargin = THIRTY_MARGIN_TOP.toDp(requireContext())
        layoutParamsForButton.marginStart = TWENTY_MARGIN_START.toDp(requireContext())
        layoutParamsForButton.marginEnd = TWENTY_MARGIN_END.toDp(requireContext())
        layoutParamsForButton.bottomMargin = TWENTY_MARGIN_BOTTOM.toDp(requireContext())

        val button = Button(requireContext())

        button.transformationMethod = null
        button.setTypeface(null, Typeface.NORMAL)
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_dark))
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        button.layoutParams = layoutParamsForButton
        button.text = getString(R.string.open_ticket_continue_button)

        if (title.isNotEmpty() && description.isNotEmpty()) {
            button.setOnClickListener {
                val answersToSave: HashMap<String, String> = hashMapOf()
                for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                    val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)

                    formUI.questions.forEach { question ->
                        if (field.id == question.id && field.visibility == View.VISIBLE) {
                            when (field) {
                                is TextInputLayout -> {
                                    answersToSave[question.name] = field.editText?.text.toString()
                                }
                                is CheckBox -> {
                                    answersToSave[question.name] = field.text.toString()
                                }
                            }
                        }
                    }
                }

                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(
                        R.id.activity_open_ticket_container,
                        OpenTicketTermsAndConditionsFragment.newInstance(title, description, formUI.name, answersToSave)
                    )
                    addToBackStack(null)
                }?.commit()
            }
        } else {
            button.setOnClickListener {
                val answersToSave: HashMap<String, String> = hashMapOf()
                for (i in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                    val field = binding.fragmentOpenTicketFormLayout.getChildAt(i)

                    formUI.questions.forEach { question ->
                        if (field.id == question.id && field.visibility == View.VISIBLE) {
                            when (field) {
                                is TextInputLayout -> {
                                    answersToSave[question.name] = field.editText?.text.toString()
                                }
                                is CheckBox -> {
                                    answersToSave[question.name] = field.text.toString()
                                }
                            }
                        }
                    }
                }
                openTicketViewModel.createTicket(formUI.name, answersToSave)
            }
        }

        binding.fragmentOpenTicketFormLayout.addView(button)
    }

    private fun setupTextInputLayoutForSpinner(adapter: ArrayAdapter<String>, question: QuestionUI): Pair<TextInputLayout, AutoCompleteTextView> {
        val layoutParamsForAutoCompleteTextView = getLayoutParamsForSpinner()

        val newContext = ContextThemeWrapper(
            requireContext(),
            R.style.Widget_MaterialComponents_TextInputLayout_FilledBox_ExposedDropdownMenu
        )

        val textInputLayout = TextInputLayout(newContext)
        textInputLayout.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.white)

        val autoCompleteTextView = AutoCompleteTextView(textInputLayout.context)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.inputType = InputType.TYPE_NULL
        autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_16_SP)
        autoCompleteTextView.layoutParams = layoutParamsForAutoCompleteTextView
        autoCompleteTextView.setPadding(
            TWELVE_PADDING_START.toDp(requireContext()),
            THIRTY_PADDING_TOP.toDp(requireContext()),
            ZERO_PADDING_RIGHT.toDp(requireContext()),
            TEN_PADDING_BOTTOM.toDp(requireContext())
        )

        textInputLayout.addView(autoCompleteTextView)
        textInputLayout.hint = question.name
        textInputLayout.id = question.id
        textInputLayout.tag = question.id

        if (question.fieldRule == FieldRule.HIDDEN_UNLESS) {
            textInputLayout.visibility = View.GONE
        }

        return Pair(textInputLayout, autoCompleteTextView)
    }

    private fun getLayoutParamsForSpinner(): LinearLayout.LayoutParams {
        val layoutParamsForAutoCompleteTextView = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            SIXTY_HEIGHT.toDp(requireContext())
        )

        layoutParamsForAutoCompleteTextView.topMargin = TWENTY_MARGIN_TOP.toDp(requireContext())
        layoutParamsForAutoCompleteTextView.marginStart = TWENTY_MARGIN_START.toDp(requireContext())
        layoutParamsForAutoCompleteTextView.marginEnd = TWENTY_MARGIN_END.toDp(requireContext())
        return layoutParamsForAutoCompleteTextView
    }

    private fun setupTextInputLayout(question: QuestionUI): TextInputLayout {
        val til = TextInputLayout(requireContext())
        val et = TextInputEditText(til.context)

        til.layoutParams = layoutParams
        til.hint = question.name
        til.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.white)
        til.id = question.id
        til.tag = question.id
        when (question.fieldType) {
            FieldType.DATE -> et.transformIntoDatePicker(requireContext(), "dd/MM/yyyy", Date())
            FieldType.INTEGER -> et.inputType = InputType.TYPE_CLASS_NUMBER
            else -> et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        til.addView(et)

        if (question.fieldRule == FieldRule.HIDDEN_UNLESS) {
            til.visibility = View.GONE
        }

        return til
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun TextInputEditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            view?.clearFocus()
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

    private fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    private fun LinearLayout.alterLayoutParams(callback: ((LinearLayout.LayoutParams) -> Unit)) {
        val layoutParams = this.layoutParams
        callback(layoutParams as LinearLayout.LayoutParams)
        this.layoutParams = layoutParams
    }

    companion object {
        private const val TWENTY_MARGIN_TOP = 20
        private const val TWENTY_MARGIN_START = 20
        private const val TWENTY_MARGIN_END = 20
        private const val TWENTY_MARGIN_BOTTOM = 20
        private const val THIRTY_MARGIN_TOP = 30
        private const val FOURTEEN_MARGIN_START = 14
        private const val FOURTEEN_MARGIN_END = 14
        private const val FORTY_MARGIN_TOP = 40
        private const val FIFTY_HEIGHT = 50
        private const val SIXTY_HEIGHT = 60
        private const val TWELVE_PADDING_START = 12
        private const val THIRTY_PADDING_TOP = 30
        private const val ZERO_PADDING_RIGHT = 0
        private const val TEN_PADDING_BOTTOM = 10
        private const val TEXT_SIZE_16_SP = 16.0f
        private const val TEXT_SIZE_18_SP = 18.0f

        fun newInstance(formUrl: String) =
            OpenTicketFormFragment().apply {
                arguments = Bundle().apply {
                    putString(FORM_URL_PARAM, formUrl)
                }
            }
    }
}
