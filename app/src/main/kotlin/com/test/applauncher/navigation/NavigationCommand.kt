package com.test.applauncher.navigation


//region import directives

import androidx.navigation.NavDirections


sealed class NavigationCommand {

    /**
     * Used for navigating to somewhere
     */
    data class To(val directions: NavDirections) : NavigationCommand()

    /**
     * Used for navigating up (or Back)
     */
    object Up : NavigationCommand()
}
