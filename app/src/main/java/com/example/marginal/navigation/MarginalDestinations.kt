package com.example.marginal.navigation

sealed class MarginalDestination(val route: String) {
    data object Splash : MarginalDestination("splash")

    data object Login : MarginalDestination("login")
    data object SignUp : MarginalDestination("signup")
    data object ForgotPassword : MarginalDestination("forgot_password")

    data object NotesList : MarginalDestination("notes_list")
    data object Settings : MarginalDestination("settings")

    // noteId is nullable in practice: absent = create mode, present = edit mode.
    // Kept as one destination + one screen, matching the UI kit's "same screen, different title" design.
    data object AddEditNote : MarginalDestination("note/edit?noteId={noteId}") {
        fun createRoute(noteId: String? = null) = "note/edit?noteId=${noteId ?: ""}"
    }

    data object NoteDetail : MarginalDestination("note/{noteId}") {
        fun createRoute(noteId: String) = "note/$noteId"
    }

    data object Scan : MarginalDestination("scan")
}
