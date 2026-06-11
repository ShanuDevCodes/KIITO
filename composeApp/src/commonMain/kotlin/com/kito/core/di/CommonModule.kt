package com.kito.core.di

import com.kito.core.database.repository.AttendanceRepository
import com.kito.core.database.repository.SectionRepository
import com.kito.core.database.repository.StudentRepository
import com.kito.core.database.repository.StudentSectionRepository
import com.kito.core.datastore.PrefsRepository
import com.kito.core.network.supabase.SupabaseRepository
import com.kito.core.platform.AppConfig
import com.kito.core.presentation.components.AppSyncUseCase
import com.kito.core.presentation.components.StartupSyncGuard
import com.kito.feature.app.presentation.AppViewModel
import com.kito.feature.attendance.presentation.AttendanceListScreenViewModel
import com.kito.feature.auth.presentation.UserSetupViewModel
import com.kito.feature.calendar.presentation.CalendarViewModel
import com.kito.feature.exam.presentation.UpcomingExamViewModel
import com.kito.feature.faculty.presentation.FacultyDetailViewModel
import com.kito.feature.faculty.presentation.FacultyScreenViewModel
import com.kito.feature.friendview.presentation.FriendViewViewmodel
import com.kito.feature.gpa.presentation.GPAViewmodel
import com.kito.feature.home.presentation.HomeViewModel
import com.kito.feature.khaoogully.presentation.KhaoogullyRepository
import com.kito.feature.khaoogully.presentation.KhaoogullyViewModel
import com.kito.feature.schedule.presentation.ScheduleScreenViewModel
import com.kito.feature.settings.presentation.SettingsViewModel
import com.kito.sap.SapPortalClient
import com.kito.sap.SapRepository
import com.kito.core.auth.AuthRepository
import com.kito.core.auth.SupabaseAuthRepository
import com.kito.core.auth.createSupabaseAuthClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

val commonModule = module {

    single(named("ApplicationScope")) { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    single<SapPortalClient>()
    single<SapRepository>()
    single<SupabaseRepository>()
    single<AttendanceRepository>()
    single<SectionRepository>()
    single<StudentRepository>()
    single<StudentSectionRepository>()
    single<PrefsRepository>()
    single<StartupSyncGuard>()
    single<AppSyncUseCase>()
    single {
        KhaoogullyRepository(
            apiKey     = AppConfig.kgAPIKey,
            baseUrl    = AppConfig.kgBaseURL
        )
    }

    // Supabase Auth (SDK) — auth/GoTrue only; separate from the raw REST client.
    // create(::fn) registers SupabaseClient as a compiler-plugin-tracked provider so that
    // get<SupabaseClient>() call sites (deep-link handlers) pass compile-time safety.
    single { create(::createSupabaseAuthClient) }
    single<AuthRepository> {
        SupabaseAuthRepository(
            client = get(),
            scope = get(named("ApplicationScope"))
        )
    }
}

val commonViewModelModule = module {

    single<AppViewModel>()
    single<UserSetupViewModel>()
    single<FriendViewViewmodel>()
    single<UpcomingExamViewModel>()
    single<FacultyScreenViewModel>()
    single<FacultyDetailViewModel>()
    single<ScheduleScreenViewModel>()
    single<SettingsViewModel>()
    single<HomeViewModel>()
    single<AttendanceListScreenViewModel>()
    single<GPAViewmodel>()
    single<CalendarViewModel>()
    single<KhaoogullyViewModel>()
}
