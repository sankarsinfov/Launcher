package com.test.applauncher.viewmodel


//region import directives

import com.test.applauncher.util.logging.Logger
import com.test.applauncher.view.fragments.DashboardFragmentDirections


class DashboardViewModel : BaseViewModel() {

    //region action handlers

    /**
     * Handler for when to show the App Launcher dialog
     */
    fun handleActionShowAppLauncher() {
        // log entry
        Logger.i("Entered handleActionShowAppLauncher()")

        try {
            navigate(DashboardFragmentDirections.actionDashboardToAppLauncher())
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting handleActionShowAppLauncher()")
        }
    }
    //endregion action handlers
}
