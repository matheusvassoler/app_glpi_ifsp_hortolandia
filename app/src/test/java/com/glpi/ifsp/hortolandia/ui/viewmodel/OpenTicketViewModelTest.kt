package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.glpi.ifsp.hortolandia.domain.CreateTicketUseCase
import com.glpi.ifsp.hortolandia.domain.GetFormUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.NullResponseBodyException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.ui.model.FormUI
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OpenTicketViewModelTest {

    @RelaxedMockK
    private lateinit var getFormUseCase: GetFormUseCase

    @RelaxedMockK
    private lateinit var createTicketUseCase: CreateTicketUseCase

    @RelaxedMockK
    lateinit var stateObserver: Observer<OpenTicketState>

    private val stateSlot = slot<OpenTicketState>()

    private val stateList: ArrayList<OpenTicketState> = arrayListOf()

    @InjectMockKs
    private lateinit var viewModel: OpenTicketViewModel

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        // must happen in this order so we can capture initial state
        every { stateObserver.onChanged(capture(stateSlot)) } answers { stateList.add(stateSlot.captured) }
        viewModel.state.observeForever(stateObserver)
    }

    @Test
    fun `viewModel SHOULD extend ViewModel`() {
        assertThat(viewModel).isInstanceOf(ViewModel::class.java)
    }

    @Test
    fun `viewModel SHOULD emit ShowLoading and ShowFormUI WHEN form is loaded successfully`() {
        // GIVEN
        val mockedFormUI = mockFormUI()
        coEvery { getFormUseCase(any()) } returns mockedFormUI

        // WHEN
        viewModel.onStart(10)

        // THEN
        coVerify { getFormUseCase(10) }
        assertThat(stateList.size).isEqualTo(2)
        assertThat(stateList[0]).isInstanceOf(OpenTicketState.ShowLoading::class.java)
        (stateList[1] as OpenTicketState.ShowFormUI).apply {
            assertThat(formUI).isEqualTo(mockedFormUI)
        }
    }

    @Test
    fun `viewModel SHOULD emit ShowLoading and ShowResponseRequestError WHEN GetFormUseCase throws ResponseRequestException`() {
        // GIVEN
        coEvery { getFormUseCase(any()) } throws ResponseRequestException()

        // WHEN
        viewModel.onStart(10)

        // THEN
        assertThat(stateList.size).isEqualTo(2)
        assertThat(stateList[0]).isInstanceOf(OpenTicketState.ShowLoading::class.java)
        assertThat(stateList[1]).isInstanceOf(OpenTicketState.ShowResponseRequestError::class.java)
    }

    @Test
    fun `viewModel SHOULD emit ShowLoading and ShowResponseRequestError WHEN form id is null`() {
        // GIVEN
        coEvery { getFormUseCase(any()) } throws ResponseRequestException()

        // WHEN
        viewModel.onStart(null)

        // THEN
        assertThat(stateList.size).isEqualTo(2)
        assertThat(stateList[0]).isInstanceOf(OpenTicketState.ShowLoading::class.java)
        assertThat(stateList[1]).isInstanceOf(OpenTicketState.ShowResponseRequestError::class.java)
    }

    @Test
    fun `viewModel SHOULD emit ShowLoading and ShowNullResponseBodyError WHEN GetFormUseCase throws NullResponseBodyException`() {
        // GIVEN
        coEvery { getFormUseCase(any()) } throws NullResponseBodyException()

        // WHEN
        viewModel.onStart(10)

        // THEN
        assertThat(stateList.size).isEqualTo(2)
        assertThat(stateList[0]).isInstanceOf(OpenTicketState.ShowLoading::class.java)
    }

    private fun mockFormUI(): FormUI {
        return FormUI(
            name = "Formulario 1",
            contentDescription = "Formulario 1 para teste",
            conditionsToHideOrShowQuestions = mockk(),
            questions = mockk(),
            validationOfCharacterSizesInFields = mockk()
        )
    }
}
