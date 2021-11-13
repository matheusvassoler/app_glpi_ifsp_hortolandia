package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.enums.ComparisonMethod
import com.glpi.ifsp.hortolandia.data.enums.FieldRule
import com.glpi.ifsp.hortolandia.data.enums.FieldType
import com.glpi.ifsp.hortolandia.data.model.Form
import com.glpi.ifsp.hortolandia.data.model.Question
import com.glpi.ifsp.hortolandia.data.model.RuleToShowQuestion
import com.glpi.ifsp.hortolandia.data.model.ValidationResponseSize
import com.glpi.ifsp.hortolandia.data.repository.form.FormRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.NullResponseBodyException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.ui.model.FormUI
import com.glpi.ifsp.hortolandia.ui.model.QuestionUI
import com.glpi.ifsp.hortolandia.ui.model.RuleToShowQuestionUI
import com.glpi.ifsp.hortolandia.ui.model.ValidationResponseSizeUI
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class GetFormUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var formRepository: FormRepository

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var getFormUseCase: GetFormUseCase

    @Test
    fun `UseCase SHOULD return form WHEN request is successful`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { formRepository.getForm(any(), any()) } returns Response.success(mockForm())

        // WHEN
        val result = getFormUseCase(10)

        // THEN
        coVerify { formRepository.getForm("12345", 10) }
        assertThat(result).isEqualTo(mockExpectedResult())
    }

    @Test(expected = NullResponseBodyException::class)
    fun `UseCase SHOULD throw NullResponseBodyException WHEN response body was null`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { formRepository.getForm(any(), any()) } returns Response.success(null)

        // WHEN
        val result = getFormUseCase(10)
    }

    @Test(expected = ResponseRequestException::class)
    fun `UseCase SHOULD throw ResponseRequestException WHEN get form was not successful`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { formRepository.getForm(any(), any()) } returns Response.error(400, "".toResponseBody())

        // WHEN
        val result = getFormUseCase(10)
    }

    @Test(expected = InternalErrorException::class)
    fun `UseCase SHOULD throw InternalErrorException WHEN session token is null`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns null

        // WHEN
        val result = getFormUseCase(10)
    }

    private fun mockForm(): Form {
        return Form(
            name = "Formulário 1",
            contentDescription = "Formulário teste",
            questions = mapOf(
                "178" to Question(
                    id = 178,
                    name = "Prontuário / Matrícula",
                    fieldType = "text",
                    values = "",
                    showRule = 1,
                    order = 2,
                    description = "&lt;p&gt;Antes de começar, precisamos saber qual seu prontuário.&lt;/p&gt;&lt;ul&gt;&lt;li&gt;Ele começa as letras ht, por exemplo: ht500233x;&lt;/li&gt;&lt;li&gt;Prontuário e Número de Matrícula são sinônimos;&lt;/li&gt;&lt;li&gt;Caso você não saiba seu prontuário, você pode se informar na Secretaria Acadêmica.&lt;/li&gt;&lt;/ul&gt;",
                    required = 1
                )
            ),
            conditionsToHideOrShowQuestions = mapOf(
                "230720" to RuleToShowQuestion(
                    id = 230720,
                    questionIdThatDisappearsOrAppearsBasedOnACondition = 181,
                    questionIdThatControlsTheCondition = 178,
                    valueThatTriggersCondition = "",
                    comparisonMethod = 2
                )
            ),
            validationOfCharacterSizesInFields = mapOf(
                "101" to ValidationResponseSize(
                    id = 101,
                    questionIdThatHasValidation = 178,
                    minimumNumberOfCharacters = "",
                    maximumNumberOfCharacters = ""
                )
            )
        )
    }

    private fun mockExpectedResult(): FormUI {
        return FormUI(
            name = "Formulário 1",
            contentDescription = "Formulário teste",
            questions = listOf(
                QuestionUI(
                    id = 178,
                    name = "Prontuário / Matrícula",
                    description = "Antes de começar, precisamos saber qual seu prontuário. Ele começa as letras ht, por exemplo: ht500233x. Prontuário e Número de Matrícula são sinônimos. Caso você não saiba seu prontuário, você pode se informar na Secretaria Acadêmica. ",
                    fieldType = FieldType.TEXT,
                    fieldRule = FieldRule.ALWAYS_DISPLAYED,
                    values = "",
                    displayOrder = 2,
                    isRequired = true
                )
            ),
            conditionsToHideOrShowQuestions = listOf(
                RuleToShowQuestionUI(
                    id = 230720,
                    questionIdThatDisappearsOrAppearsBasedOnACondition = 181,
                    questionIdThatControlsTheCondition = 178,
                    valueThatTriggersCondition = "",
                    comparisonMethod = ComparisonMethod.DIFFERENT
                )
            ),
            validationOfCharacterSizesInFields = listOf(
                ValidationResponseSizeUI(
                    id = 101,
                    questionIdThatHasValidation = 178,
                    minimumNumberOfCharacters = null,
                    maximumNumberOfCharacters = null
                )
            )
        )
    }
}
