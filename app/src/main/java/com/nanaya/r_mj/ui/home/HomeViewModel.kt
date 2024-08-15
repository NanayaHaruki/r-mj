package com.nanaya.r_mj.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.nanaya.r_mj.data.MahjongSchoolRepo
import com.nanaya.r_mj.data.local.LocalMahjongSchool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UIState(
    val isLoading: Boolean,
    val schools: List<LocalMahjongSchool>,
    val detail: LocalMahjongSchool?
)

class HomeViewModel(val repo: MahjongSchoolRepo) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState(false, emptyList(), null))
    val uiState: StateFlow<UIState> = _uiState

    init {
        viewModelScope.launch {
            repo.findSchoolsByArea()
            _uiState.update { it.copy(isLoading = true) }
            repo.fetchSchoolsFromServer()
            _uiState.update { it.copy(isLoading = false) }
        }
        viewModelScope.launch {
            repo.observerSchools().collect { newSchools ->
                _uiState.update { it.copy(schools = newSchools) }
            }
        }
    }

    fun navToDetail(localMahjongSchool: LocalMahjongSchool) {
        _uiState.update { it.copy(isLoading = true, detail = localMahjongSchool) }
        viewModelScope.launch {
            val local = repo.fetchSchoolDetailFromServer(localMahjongSchool.cid)
            if (local != null) {
                _uiState.update { it.copy(isLoading = false, detail = local) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }

    }

    fun navToList() {
        _uiState.update { it.copy(detail = null) }
    }

    fun selectArea(s: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repo.findSchoolsByArea(if (s == "全国") null else s)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun searchSchools(content: String) {
        LogUtils.d("search $content")
        viewModelScope.launch {
            _uiState.update {it.copy(isLoading = true)}
            if(content.isEmpty()){
                repo.findSchoolsByArea()
            }else {
                repo.findSchoolsByNameOrCity(content)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"

        @Suppress("UNCHECKED_CAST")
        fun providerFactory(repo: MahjongSchoolRepo) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(repo) as T
            }
        }
    }
}