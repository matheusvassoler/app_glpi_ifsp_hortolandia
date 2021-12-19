package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.enums.ComparisonMethod
import com.glpi.ifsp.hortolandia.data.enums.FieldRule
import com.glpi.ifsp.hortolandia.data.enums.FieldType
import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.data.model.Form
import com.glpi.ifsp.hortolandia.data.model.Item
import com.glpi.ifsp.hortolandia.data.model.Question
import com.glpi.ifsp.hortolandia.data.model.RuleToShowQuestion
import com.glpi.ifsp.hortolandia.data.model.ValidationResponseSize
import com.glpi.ifsp.hortolandia.data.repository.form.FormRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.NullResponseBodyException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.utils.removeUnicodeHtmlTag
import com.glpi.ifsp.hortolandia.ui.model.FormUI
import com.glpi.ifsp.hortolandia.ui.model.LocationUI
import com.glpi.ifsp.hortolandia.ui.model.QuestionUI
import com.glpi.ifsp.hortolandia.ui.model.RuleToShowQuestionUI
import com.glpi.ifsp.hortolandia.ui.model.ValidationResponseSizeUI
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.IllegalArgumentException
import java.util.HashMap
import retrofit2.Response

class GetFormUseCase(
    private val formRepository: FormRepository,
    private val sessionUseCase: SessionUseCase,
    private val getItemUseCase: GetItemUseCase,
    private val getLocationUseCase: GetLocationUseCase
) {

    suspend operator fun invoke(formId: Int): FormUI {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        val response = formRepository.getForm(sessionToken, formId)

        if (response.isSuccessful) {
            return getFormUI(response)
        } else {
            throw ResponseRequestException()
        }
    }

    private suspend fun getFormUI(response: Response<Form>): FormUI {
        val form = response.body()
        if (form != null) {
            return FormUI(
                name = form.name,
                contentDescription = form.contentDescription,
                questions = getQuestions(form.questions),
                conditionsToHideOrShowQuestions = getConditionsToHideOrShowQuestions(
                    form.conditionsToHideOrShowQuestions
                ),
                validationOfCharacterSizesInFields = getValidationOfCharacterSizesInFields(
                    form.validationOfCharacterSizesInFields
                )
            )
        } else {
            throw NullResponseBodyException()
        }
    }

    private suspend fun getQuestions(questions: Map<String, Question>): List<QuestionUI> {
        val questionsUI = ArrayList<QuestionUI>()
        for (question in questions.values) {
            questionsUI.add(
                QuestionUI(
                    id = question.id,
                    name = question.name,
                    description = removeHtmlTag(question),
                    fieldType = FieldType.valueOf(question.fieldType.uppercase()),
                    values = question.values,
                    fieldRule = getFieldRule(question.showRule),
                    displayOrder = question.order,
                    isRequired = question.required == 1,
                    items = getItems(question.values),
                    locations = getLocations(question.values)
                )
            )
        }
        return questionsUI
    }

    private fun getConditionsToHideOrShowQuestions(
        conditionsToHideOrShowQuestions: Map<String, RuleToShowQuestion>
    ): List<RuleToShowQuestionUI> {
        val rulesToShowQuestions = ArrayList<RuleToShowQuestionUI>()
        for (rule in conditionsToHideOrShowQuestions.values) {
            rulesToShowQuestions.add(
                RuleToShowQuestionUI(
                    id = rule.id,
                    questionIdThatDisappearsOrAppearsBasedOnACondition =
                        rule.questionIdThatDisappearsOrAppearsBasedOnACondition,
                    questionIdThatControlsTheCondition = rule.questionIdThatControlsTheCondition,
                    valueThatTriggersCondition = rule.valueThatTriggersCondition,
                    comparisonMethod = getComparisonMethod(rule.comparisonMethod)
                )
            )
        }
        return rulesToShowQuestions
    }

    private fun getValidationOfCharacterSizesInFields(
        validationOfCharacterSizesInFields: Map<String, ValidationResponseSize>
    ): List<ValidationResponseSizeUI> {
        val validationsOfCharacterSizes = ArrayList<ValidationResponseSizeUI>()
        for (validation in validationOfCharacterSizesInFields.values) {
            validationsOfCharacterSizes.add(
                ValidationResponseSizeUI(
                    id = validation.id,
                    questionIdThatHasValidation = validation.questionIdThatHasValidation,
                    minimumNumberOfCharacters = stringToInt(validation.minimumNumberOfCharacters),
                    maximumNumberOfCharacters = stringToInt(validation.maximumNumberOfCharacters)
                )
            )
        }
        return validationsOfCharacterSizes
    }

    private fun stringToInt(value: String): Int? {
        return if (value == "") null else value.toInt()
    }

    private fun getComparisonMethod(comparisonMethod: Int): ComparisonMethod {
        return when (comparisonMethod) {
            EQUAL -> ComparisonMethod.EQUAL
            DIFFERENT -> ComparisonMethod.DIFFERENT
            LESS -> ComparisonMethod.LESS
            BIGGER -> ComparisonMethod.BIGGER
            LESS_OR_EQUAL -> ComparisonMethod.LESS_OR_EQUAL
            else -> ComparisonMethod.BIGGER_OR_EQUAL
        }
    }

    private fun getFieldRule(rule: Int): FieldRule {
        return when (rule) {
            ALWAYS_DISPLAYED -> FieldRule.ALWAYS_DISPLAYED
            HIDDEN_UNLESS -> FieldRule.HIDDEN_UNLESS
            else -> FieldRule.DISPLAYED_UNLESS
        }
    }

    private suspend fun getItems(values: String?): List<Item>? {
        if (values != null) {
            return if (values.isNotBlank()) {
                try {
                    getItemFromJsonStringConvertedToObject(values)
                } catch (e: JsonSyntaxException) {
                    tryToGetItemFromItemType(values)
                } catch (e: IllegalStateException) {
                    tryToGetItemFromItemType(values)
                } catch (e: IllegalArgumentException) {
                    null
                }
            } else {
                null
            }
        } else {
            return null
        }
    }

    private suspend fun tryToGetItemFromItemType(values: String?) =
        if (values != null) {
            try {
                getItemFromItemType(values)
            } catch (e: IllegalArgumentException) {
                null
            }
        } else {
            null
        }

    private suspend fun getItemFromJsonStringConvertedToObject(values: String?): List<Item>? {
        val mapObj: Map<String, String> = Gson().fromJson(
            values,
            object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )
        val itemType = mapObj.getValue("itemtype")
        return getItemFromItemType(itemType)
    }

    private suspend fun getItemFromItemType(values: String) =
        if (ItemType.valueOf(values.uppercase()) != ItemType.LOCATION) {
            getItemUseCase(ItemType.valueOf(values.uppercase()))
        } else {
            null
        }

    private suspend fun getLocations(values: String?): List<LocationUI>? {
        if (values != null) {
            return if (values.isNotBlank()) {
                try {
                    getLocationFromJsonStringConvertedToObject(values)
                } catch (e: JsonSyntaxException) {
                    tryToGetLocationFromItemType(values)
                } catch (e: IllegalStateException) {
                    tryToGetLocationFromItemType(values)
                } catch (e: IllegalArgumentException) {
                    null
                }
            } else {
                null
            }
        } else {
            return null
        }
    }

    private suspend fun tryToGetLocationFromItemType(
        values: String?
    ): List<LocationUI>? {
        return if (values != null) {
            try {
                return getLocationFromItemType(values)
            } catch (e: IllegalArgumentException) {
                null
            }
        } else {
            null
        }
    }

    private suspend fun getLocationFromJsonStringConvertedToObject(values: String?): List<LocationUI>? {
        val mapObj: Map<String, String> = Gson().fromJson(
            values,
            object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )
        val itemType = mapObj.getValue("itemtype")
        return getLocationFromItemType(itemType)
    }

    private suspend fun getLocationFromItemType(values: String): List<LocationUI>? {
        return if (ItemType.valueOf(values.uppercase()) == ItemType.LOCATION) {
            getLocationUseCase()
        } else {
            null
        }
    }

    private fun removeHtmlTag(question: Question): String {
        return if (!question.name.lowercase().contains("termos e condições")) {
            question.description.removeUnicodeHtmlTag()
        } else {
            question.description
        }
    }

    companion object {
        private const val EQUAL = 1
        private const val DIFFERENT = 2
        private const val LESS = 3
        private const val BIGGER = 4
        private const val LESS_OR_EQUAL = 5
        private const val ALWAYS_DISPLAYED = 1
        private const val HIDDEN_UNLESS = 2
    }
}
