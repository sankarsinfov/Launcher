package com.test.applauncher.viewmodel


//region import directives

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.test.applauncher.navigation.NavigationCommand
import com.test.applauncher.util.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    //region data members

    /**
     * Navigation command - SingleLiveEvent
     */
    private val _navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()

    /**
     * Observable navigation data
     */
    val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    //endregion data members


    //region methods

    /**
     * Helper method for navigating
     */
    fun navigate(navDirections: NavDirections) {
        _navigationCommand.postValue(NavigationCommand.To(navDirections))
    }

    /**
     * Helper method for navigating up
     */
    fun navigateUp() {
        _navigationCommand.postValue(NavigationCommand.Up)
    }
    //endregion methods
}