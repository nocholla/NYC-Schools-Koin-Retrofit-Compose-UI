package com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.usecase

import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.model.School
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.repository.SchoolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSchoolsUseCase : KoinComponent {
    private val repository: SchoolRepository by inject()

    suspend operator fun invoke(): Result<Flow<List<School>>> {
        return try {
            val schoolsFlow = repository.getSchools().map { schools ->
                if (schools.isEmpty()) throw Exception("No schools found")
                schools
            }.catch { e -> throw e }
            Result.success(schoolsFlow)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}