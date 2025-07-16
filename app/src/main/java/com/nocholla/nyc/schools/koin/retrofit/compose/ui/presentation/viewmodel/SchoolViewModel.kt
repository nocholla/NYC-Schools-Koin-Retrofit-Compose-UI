package com.nocholla.nyc.schools.koin.retrofit.compose.ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.model.School
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.model.Score
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.usecase.GetSchoolsUseCase
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.usecase.GetScoresUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class SchoolUiState(
    val schools: List<School> = emptyList(),
    val scores: List<Score> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SchoolViewModel : ViewModel(), KoinComponent {
    private val getSchoolsUseCase: GetSchoolsUseCase by inject()
    private val getScoresUseCase: GetScoresUseCase by inject()

    private val _uiState = MutableStateFlow(SchoolUiState(isLoading = true))
    val uiState: StateFlow<SchoolUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val schoolsResult = getSchoolsUseCase()
                val scoresResult = getScoresUseCase()
                schoolsResult.getOrThrow().collect { schools ->
                    _uiState.update { it.copy(schools = schools, isLoading = false) }
                }
                scoresResult.getOrThrow().collect { scores ->
                    _uiState.update { it.copy(scores = scores, isLoading = false) }
                }
            } catch (e: retrofit2.HttpException) {
                _uiState.update {
                    it.copy(
                        error = "API Error: ${e.code()} - ${e.message()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.localizedMessage ?: "Unknown error",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchSchoolDetails(dbn: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val schoolsResult = getSchoolsUseCase()
                val schoolsFlow = schoolsResult.getOrThrow()
                val schools = schoolsFlow.first()
                val filteredSchools = schools.filter { it.dbn == dbn }
                _uiState.update {
                    it.copy(schools = filteredSchools.ifEmpty { schools }, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.localizedMessage ?: "Failed to load school details",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getSchoolByDbn(dbn: String?): School? {
        return uiState.value.schools.find { it.dbn == dbn }
    }
}