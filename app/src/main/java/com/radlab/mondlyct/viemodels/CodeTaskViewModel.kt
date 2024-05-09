package com.radlab.mondlyct.viemodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlab.mondlyct.models.Item
import com.radlab.mondlyct.repo.CodeTaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CodeTaskState {
    data class Loading(val message: String = "Loading...") : CodeTaskState()
    data class Success(val data: List<Item>) : CodeTaskState()
    data class Error(val errorMessage: String) : CodeTaskState()
}

sealed class CodeTaskIntent {
    data object LoadData : CodeTaskIntent()
}

class CodeTaskViewModel(private val codeTaskRepository: CodeTaskRepository) : ViewModel() {

    private val _state = MutableStateFlow<CodeTaskState>(CodeTaskState.Loading())
    val state: StateFlow<CodeTaskState> = _state

    init {
        processIntent(CodeTaskIntent.LoadData)
    }

    fun processIntent(intent: CodeTaskIntent) {
        when (intent) {
            is CodeTaskIntent.LoadData -> fetchCodeTask()
        }
    }

    private fun fetchCodeTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = codeTaskRepository.getCodeTaskList()
            if (result.isSuccessful) {
                result.body()?.let { data ->
                    _state.value = CodeTaskState.Success(data)
                }
            } else {
                _state.value = CodeTaskState.Error("An error occurred")
            }
        }
    }
}
