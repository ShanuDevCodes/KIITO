package com.kito.feature.gpa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.database.entity.StudentEntity
import com.kito.core.database.repository.StudentRepository
import com.kito.core.datastore.PrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class GPAViewmodel(
    prefs: PrefsRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    val roll = prefs.userRollFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    private val _student = MutableStateFlow<StudentEntity?>(null)
    val student = _student.asStateFlow()

    private val _branch = MutableStateFlow("CSE")
    val branch = _branch.asStateFlow()

    private val _semester = MutableStateFlow(1)
    val semester = _semester.asStateFlow()

    init {
        viewModelScope.launch {
            roll.collect { rollNumber ->

                if (rollNumber.isEmpty()) return@collect

                val student = studentRepository.getStudentByRoll(rollNumber)
                _student.value = student

                configureGpaDefaults(rollNumber, student)
            }
        }
    }

    private fun configureGpaDefaults(
        roll: String,
        student: StudentEntity?
    ) {

        // Branch from DB if available
        val branch = student?.section?.substringBefore("-") ?: "CSE"

        val semester = deriveSemesterFromRoll(roll)

        _branch.value = branch
        _semester.value = semester
    }

    private fun deriveSemesterFromRoll(roll: String): Int {

        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val currentYear = now.year
        val month = now.month.number

        val joinYear = ("20" + roll.take(2)).toInt()

        val yearDiff = currentYear - joinYear

        val term = if (month in 7..11) "010" else "020"

        return when (yearDiff) {
            1 -> if (term == "010") 1 else 2
            2 -> if (term == "010") 3 else 4
            3 -> if (term == "010") 5 else 6
            4 -> if (term == "010") 7 else 8
            else -> 1
        }
    }

    fun updateSemester(semester: Int) {
        _semester.value = semester
    }

    fun updateBranch(branch: String) {
        _branch.value = branch
    }
}