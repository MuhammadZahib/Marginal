package com.example.marginal.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * Rapid repeated taps on a back/navigate button can fire navigate()/popBackStack()
 * multiple times before the first transition finishes — the second call then acts
 * on a back stack that's mid-transition, which can throw and leave Compose's nav
 * host in a blank, frozen state (fixable only by force-closing the app).
 *
 * The current back stack entry's lifecycle sits at STARTED while a transition is
 * in flight, and only reaches RESUMED once it's fully settled — so checking that
 * state naturally blocks the extra taps without needing a manual debounce timer.
 * This is Google's own recommended pattern for this exact symptom.
 */
private fun NavController.isReadyToNavigate(): Boolean =
    currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavController.navigateSafe(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    if (isReadyToNavigate()) {
        navigate(route, builder)
    }
}

fun NavController.popBackStackSafe() {
    if (isReadyToNavigate()) {
        popBackStack()
    }
}
