package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.domain.GetFormUseCase
import com.glpi.ifsp.hortolandia.domain.GetItemUseCase
import com.glpi.ifsp.hortolandia.domain.GetLocationUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.NullResponseBodyException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import kotlinx.coroutines.launch

class OpenTicketViewModel(
    private val getFormUseCase: GetFormUseCase
) : ViewModel() {

    private var _state = MutableLiveData<OpenTicketState>()
    val state: LiveData<OpenTicketState>
        get() = _state

    fun onStart(formId: Int?) {
        viewModelScope.launch {
            try {
                _state.value = OpenTicketState.ShowLoading
                if (formId == null) {
                    throw ResponseRequestException()
                }
                val formUI = getFormUseCase(formId)
                _state.value = OpenTicketState.ShowFormUI(formUI)
            } catch (e: ResponseRequestException) {
                _state.value = OpenTicketState.ShowResponseRequestError
            } catch (e: NullResponseBodyException) {
                _state.value = OpenTicketState.ShowNullResponseBodyError
            }
        }
    }

    // Ideia para obter o item e passar para o campo correto
    // Passar o tipo de campo no método junto com o item que se quer obter
    // Retonar um state que contenha os itens e o tipo de campo
    fun getItems(itemType: String) {

    }
}
