package com.kito.core.presentation.components

import com.kito.core.database.entity.toAttendanceEntity
import com.kito.core.database.repository.AttendanceRepository
import com.kito.core.database.repository.SectionRepository
import com.kito.core.database.repository.StudentRepository
import com.kito.core.database.repository.StudentSectionRepository
import com.kito.core.network.supabase.SupabaseRepository
import com.kito.core.platform.AppSyncTrigger
import com.kito.sap.AttendanceResult
import com.kito.sap.SapRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope

class AppSyncUseCase(
    private val syncTrigger: AppSyncTrigger,
    private val supabaseRepository: SupabaseRepository,
    private val studentRepository: StudentRepository,
    private val sectionRepository: SectionRepository,
    private val studentSectionRepository: StudentSectionRepository,
    private val attendanceRepository: AttendanceRepository,
    private val sapRepository: SapRepository,
) {

    suspend fun syncAll(
        roll: String,
        sapPassword: String,
        year: String,
        term: String
    ): Result<Unit> = supervisorScope {
        try {
            val student = supabaseRepository.getStudentByRoll(roll)
            val timetable = supabaseRepository.getTimetableForStudent(
                section = student.section,
                batch = student.batch
            )
            coroutineScope {
                async { studentRepository.insertStudent(listOf(student)) }
                async { sectionRepository.insertSection(timetable) }
            }
            if (sapPassword.isNotEmpty()) {
                when (
                    val response = sapRepository.login(
                        username = roll,
                        password = sapPassword,
                        academicYear = year,
                        termCode = term
                    )
                ) {
                    is AttendanceResult.Success -> {
                        attendanceRepository.insertAttendance(
                            response.data.subjects.map {
                                it.toAttendanceEntity(year, term)
                            }
                        )
                    }
                    is AttendanceResult.Error -> {
                        throw IllegalStateException(response.message)
                    }
                }
            }
            val sections =
                studentSectionRepository.getAllScheduleForStudent(rollNo = roll).first()
            syncTrigger.onSyncComplete(roll, sections)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
