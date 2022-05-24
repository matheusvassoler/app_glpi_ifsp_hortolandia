package com.glpi.ifsp.hortolandia.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.glpi.ifsp.hortolandia.ui.builder.CheckBoxBuilder
import com.glpi.ifsp.hortolandia.ui.builder.SpinnerBuilder
import com.glpi.ifsp.hortolandia.ui.builder.TextInputLayoutBuilder
import com.glpi.ifsp.hortolandia.ui.builder.TextViewBuilder
import com.glpi.ifsp.hortolandia.ui.event.OpenTicketEvent
import com.glpi.ifsp.hortolandia.ui.model.QuestionUI
import com.glpi.ifsp.hortolandia.ui.model.RuleToShowQuestionUI
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import com.glpi.ifsp.hortolandia.ui.viewmodel.OpenTicketViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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
    private lateinit var allFormQuestions: List<QuestionUI>
    private lateinit var allFormConditions: List<RuleToShowQuestionUI>

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

        //layoutParams.topMargin = TWENTY_MARGIN_TOP.toDp(requireContext())
        //layoutParams.marginStart = TWENTY_MARGIN_START.toDp(requireContext())
        //layoutParams.marginEnd = TWENTY_MARGIN_END.toDp(requireContext())
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
                    createFormHeader(it.formUI.name)
                    allFormQuestions = it.formUI.questions
                    allFormConditions = it.formUI.conditionsToHideOrShowQuestions
                    createFields(it)
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

    private fun createFields(showFormUIState: OpenTicketState.ShowFormUI) {
        showFormUIState.formUI.questions.forEach { question ->
            val conditionsControlledByField: ArrayList<RuleToShowQuestionUI> = arrayListOf()
            for (condition in allFormConditions) {
                if (question.id == condition.questionIdThatControlsTheCondition) {
                    conditionsControlledByField.add(condition)
                }
            }
            when (question.fieldType) {
                FieldType.TEXT -> {
                    createViewToFieldTypeText(question)
                }
                FieldType.DATE -> {
                    createViewToFieldTypeText(question)
                }
                FieldType.EMAIL -> {
                    createViewToFieldTypeText(question)
                }
                FieldType.CHECKBOXES -> {
                    createViewToFieldTypeCheckbox(question, conditionsControlledByField)
                }
                FieldType.SELECT -> {
                    createViewForFieldTypeSelect(question, conditionsControlledByField)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createFormHeader(formName: String) {
        val textView = TextViewBuilder(requireContext(), formName, MATCH_PARENT, WRAP_CONTENT)
            .setTypeFace(Typeface.DEFAULT_BOLD)
            .setTextColor(R.color.black)
            .setTextSize(TEXT_SIZE_18_SP)
            .setTopMargin(SPACING_30)
            .setLeftMargin(SPACING_20)
            .setRightMargin(SPACING_20)
            .build()
        binding.fragmentOpenTicketFormLayout.addView(textView)
    }

    private fun createViewToFieldTypeText(question: QuestionUI) {
        val textInputLayout = TextInputLayoutBuilder(requireContext())
            .setLeftMargin(SPACING_20)
            .setTopMargin(SPACING_20)
            .setRightMargin(SPACING_20)
            .setHint(question.name)
            .setTag(question.id)
            .setFieldType(question.fieldType)
            .setBackgroundColor(R.color.white)
            .build()
        hideInitiallyFieldThatHasHiddenUnlessRule(question.fieldRule, textInputLayout)
        binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
    }

    private fun createViewToFieldTypeCheckbox(
        question: QuestionUI,
        conditionsControlledByField: List<RuleToShowQuestionUI>
    ) {
        if (question.values?.contains("\r\n") == true) {
            createCheckBoxWhenQuestionHasMoreThanOneOption(
                question.values,
                question,
                conditionsControlledByField
            )
        } else {
            createCheckBox(question, conditionsControlledByField)
        }
    }

    private fun createCheckBoxWhenQuestionHasMoreThanOneOption(
        values: String,
        question: QuestionUI,
        conditionsControlledByField: List<RuleToShowQuestionUI>
    ) {
        val splittedText = values.split("\r\n")
        splittedText.forEach { questionInfo ->
            val onCheckedChangeListener =
                getOnCheckedChangeListener(conditionsControlledByField, question)
            val checkBox = CheckBoxBuilder(requireContext())
                .setText(questionInfo)
                .setTag(question.id)
                .setLeftMargin(SPACING_14)
                .setTopMargin(SPACING_20)
                .setRightMargin(SPACING_14)
                .setTextColor(R.color.gray_dark_for_field_hint)
                .setTextSize(TEXT_SIZE_16_SP)
                .setOnCheckedChangeListener(onCheckedChangeListener)
                .build()
            hideInitiallyFieldThatHasHiddenUnlessRule(question.fieldRule, checkBox)
            binding.fragmentOpenTicketFormLayout.addView(checkBox)
        }
    }

    private fun createCheckBox(
        question: QuestionUI,
        conditionsControlledByField: List<RuleToShowQuestionUI>
    ) {
        val onCheckedChangeListener =
            getOnCheckedChangeListener(conditionsControlledByField, question)
        val checkBox = CheckBoxBuilder(requireContext())
            .setText(question.values ?: "")
            .setTag(question.id)
            .setLeftMargin(SPACING_14)
            .setTopMargin(SPACING_40)
            .setRightMargin(SPACING_14)
            .setTextColor(R.color.gray_dark_for_field_hint)
            .setTextSize(TEXT_SIZE_16_SP)
            .setOnCheckedChangeListener(onCheckedChangeListener)
            .build()
        hideInitiallyFieldThatHasHiddenUnlessRule(question.fieldRule, checkBox)
        binding.fragmentOpenTicketFormLayout.addView(checkBox)
    }

    private fun createViewForFieldTypeSelect(
        question: QuestionUI,
        conditionsControlledByField: List<RuleToShowQuestionUI>
    ) {
        val optionsToSelect = getValueForFieldTypeSelect(question)
        val onItemClickListener = getOnItemClickListener(conditionsControlledByField, question)
        val textInputLayout = buildSpinnerView(optionsToSelect, question, onItemClickListener)
        hideInitiallyFieldThatHasHiddenUnlessRule(question.fieldRule, textInputLayout)
        binding.fragmentOpenTicketFormLayout.addView(textInputLayout)
    }

    private fun getValueForFieldTypeSelect(question: QuestionUI): ArrayList<String> {
        val optionsToSelect =
            question.values?.split("\r\n")?.filter { item ->
                item != ""
            } as ArrayList<String>
        return optionsToSelect
    }

    private fun getOnCheckedChangeListener(
        conditionsControlledByField: List<RuleToShowQuestionUI>,
        question: QuestionUI
    ): CompoundButton.OnCheckedChangeListener? {
        var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
        if (conditionsControlledByField.isNotEmpty()) {
            onCheckedChangeListener =
                CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked) {
                        hideOrShowField(
                            conditionsControlledByField,
                            question,
                            compoundButton.text.toString()
                        )
                    } else {
                        hideOrShowField(conditionsControlledByField, question, "")
                    }
            }
        }
        return onCheckedChangeListener
    }

    private fun getOnItemClickListener(
        conditionsControlledByField: List<RuleToShowQuestionUI>,
        question: QuestionUI
    ): AdapterView.OnItemClickListener? {
        var onItemClickListener: AdapterView.OnItemClickListener? = null
        if (conditionsControlledByField.isNotEmpty()) {
            onItemClickListener =
                AdapterView.OnItemClickListener { _, selectedView, _, _ ->
                    val selectedOption = (selectedView as TextView).text.toString()
                    hideOrShowField(conditionsControlledByField, question, selectedOption)
                }
        }
        return onItemClickListener
    }

    private fun hideOrShowField(
        conditionsControlledByField: List<RuleToShowQuestionUI>,
        question: QuestionUI,
        answerEnteredInTheField: String
    ) {
        for (condition in conditionsControlledByField) {
            if (condition.questionIdThatControlsTheCondition == question.id) {
                val controlledQuestion = getQuestionById(
                    allFormQuestions,
                    condition.questionIdThatDisappearsOrAppearsBasedOnACondition
                )

                checkFieldValueTriggersCondition(
                    condition,
                    answerEnteredInTheField,
                    controlledQuestion
                )
            }
        }
    }

    private fun checkFieldValueTriggersCondition(
        condition: RuleToShowQuestionUI,
        answerEnteredInTheField: String,
        controlledQuestion: QuestionUI?
    ) {
        if (condition.valueThatTriggersCondition == answerEnteredInTheField) {

            if (controlledQuestion?.fieldRule == FieldRule.HIDDEN_UNLESS) {
                //  TODO - Exibir questão
                setFieldVisibility(controlledQuestion, View.VISIBLE)
                showQuestion(arrayListOf(controlledQuestion))
            } else {
                // TODO - Ocultar questão
                setFieldVisibility(controlledQuestion, View.GONE)
                hideQuestion(arrayListOf(controlledQuestion))
            }
        } else {

            if (controlledQuestion?.fieldRule == FieldRule.HIDDEN_UNLESS) {
                //  TODO - Exibir questão
                setFieldVisibility(controlledQuestion, View.GONE)
                hideQuestion(arrayListOf(controlledQuestion))
            } else {
                // TODO - Exibir questão
                setFieldVisibility(controlledQuestion, View.VISIBLE)
                showQuestion(arrayListOf(controlledQuestion))
            }
        }
    }

    private fun setFieldVisibility(controlledQuestion: QuestionUI?, visibility: Int) {
        for (j in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
            val view = binding.fragmentOpenTicketFormLayout.getChildAt(j)
            if (view.tag == controlledQuestion?.id) {
                view.visibility = visibility
            }
        }
    }

    private fun getQuestionById(questions: List<QuestionUI>, id: Int): QuestionUI? {
        for (question in questions) {
            if (question.id == id) {
                return question
            }
        }
        return null
    }

    private fun hideQuestion(questionsThatCanTriggerConditions: List<QuestionUI?>) {
        val questionsControlledByOtherQuestion: ArrayList<QuestionUI?> = arrayListOf()
        for (question in questionsThatCanTriggerConditions) {
            for (condition in allFormConditions) {
                if (condition.questionIdThatControlsTheCondition == question?.id) {
                    if (question.fieldRule == FieldRule.HIDDEN_UNLESS) {
                        val controlledQuestion = getQuestionById(allFormQuestions, condition.questionIdThatDisappearsOrAppearsBasedOnACondition)

                        for(j in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                            val view = binding.fragmentOpenTicketFormLayout.getChildAt(j)
                            if (view.tag == controlledQuestion?.id) {
                                view.visibility = View.GONE
                            }
                        }

                        questionsControlledByOtherQuestion.add(controlledQuestion)
                        println("Questão ${controlledQuestion?.name} foi ocultada")
                    }
                }
            }
        }

        if (questionsControlledByOtherQuestion.isNotEmpty()) {
            hideQuestion(questionsControlledByOtherQuestion)
        }
    }

    private fun showQuestion(questionsThatCanTriggerConditions: List<QuestionUI?>) {
        val questionsControlledByOtherQuestion: ArrayList<QuestionUI?> = arrayListOf()
        for (question in questionsThatCanTriggerConditions) {
            for (condition in allFormConditions) {
                if (condition.questionIdThatControlsTheCondition == question?.id) {
                    for(j in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                        val view = binding.fragmentOpenTicketFormLayout.getChildAt(j)

                        if (view.tag == question.id) {
                            if (view is TextInputLayout) {
                                if (question.fieldRule == FieldRule.HIDDEN_UNLESS && view.editText?.text.toString() == condition.valueThatTriggersCondition) {
                                    val controlledQuestion = getQuestionById(allFormQuestions, condition.questionIdThatDisappearsOrAppearsBasedOnACondition)
                                    questionsControlledByOtherQuestion.add(controlledQuestion)
                                    println("Questão ${controlledQuestion?.name} foi exibida")

                                    for(m in 0 until binding.fragmentOpenTicketFormLayout.childCount) {
                                        val v = binding.fragmentOpenTicketFormLayout.getChildAt(m)
                                        if (v.tag != null) {
                                            if (v.tag == controlledQuestion?.id) {
                                                v.visibility = View.VISIBLE
                                                println("BLAHJSJHAJSAJHSJASHJASAHJSAHSJ")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (questionsControlledByOtherQuestion.isNotEmpty()) {
            showQuestion(questionsControlledByOtherQuestion)
        }
    }

    private fun buildSpinnerView(
        optionsToSelect: ArrayList<String>,
        question: QuestionUI,
        onItemClickListener: AdapterView.OnItemClickListener?
    ): TextInputLayout {
        return SpinnerBuilder(requireContext(), optionsToSelect, MATCH_PARENT, SPACING_60.toDp())
            .setHint(question.name)
            .setTag(question.id)
            .setBackgroundColor(R.color.white)
            .setTextSize(TEXT_SIZE_16_SP)
            .setLeftMargin(SPACING_20)
            .setTopMargin(SPACING_20)
            .setRightMargin(SPACING_20)
            .setLeftPadding(SPACING_12)
            .setTopPadding(SPACING_30)
            .setBottomPadding(SPACING_10)
            .setOnItemClickListener(onItemClickListener)
            .build()
    }

    private fun hideInitiallyFieldThatHasHiddenUnlessRule(
        fieldRule: FieldRule,
        view: View
    ) {
        if (fieldRule == FieldRule.HIDDEN_UNLESS) {
            view.visibility = View.GONE
        }
    }

    private fun getLayoutParams(width: Int? = null, height: Int? = null): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            width ?: ViewGroup.LayoutParams.MATCH_PARENT,
            height ?: ViewGroup.LayoutParams.WRAP_CONTENT
        )
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

    private fun Int.toDp(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), requireContext().resources.displayMetrics
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

        private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
        private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
        private const val SPACING_0 = 0
        private const val SPACING_10 = 10
        private const val SPACING_12 = 12
        private const val SPACING_14 = 14
        private const val SPACING_20 = 20
        private const val SPACING_30 = 30
        private const val SPACING_40 = 40
        private const val SPACING_60 = 60

        fun newInstance(formUrl: String) =
            OpenTicketFormFragment().apply {
                arguments = Bundle().apply {
                    putString(FORM_URL_PARAM, formUrl)
                }
            }
    }
}
