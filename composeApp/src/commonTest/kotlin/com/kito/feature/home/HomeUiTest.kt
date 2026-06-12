package com.kito.feature.home

// HomeUiTest — SKIPPED
//
// HomeContent composes ScheduleCard (mesh color animators + clock while(true)) and
// AttendanceBarCard (AttendanceBarGraph + OverallAttendanceCard, 8 while(true) loops).
// StandardTestDispatcher cannot settle waitForIdle with these loops present.
//
// To unblock: extract enableAnimations through the full ScheduleCard → AttendanceBarGraph →
// OverallAttendanceCard chain (8+ composables), or gate all infinite LaunchedEffects
// on a CompositionLocal. Tracked as tech debt.
//
// The ViewModel tests (HomeViewModelTest) cover all state logic.
