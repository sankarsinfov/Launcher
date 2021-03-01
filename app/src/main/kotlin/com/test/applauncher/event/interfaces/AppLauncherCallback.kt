package com.test.applauncher.event.interfaces


//region import directives

import com.test.applauncher.viewmodel.AppLauncherViewModel

//endregion import directives



interface AppLauncherCallback {

    /**
     * Callback for when the user selects to launch an App
     *
     * @param appInfo AppInfo object for the App card that was selected
     */
    fun onAppClicked(appInfo: AppLauncherViewModel.App.AppInfo)
}