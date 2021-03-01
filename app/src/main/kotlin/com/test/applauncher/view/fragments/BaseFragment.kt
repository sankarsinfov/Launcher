package com.test.applauncher.view.fragments


//region import directives

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.test.applauncher.util.logging.Logger
import com.test.applauncher.navigation.NavigationCommand
import com.test.applauncher.viewmodel.BaseViewModel

//endregion import directives



abstract class BaseFragment : Fragment() {

    //region data members

    /**
     * Reference to our view model
     */
    protected lateinit var viewModel: BaseViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // log entry
        Logger.i("Entered onActivityCreated(${activity ?: "Unknown"})")

        try {
            // call base class implementation
            super.onActivityCreated(savedInstanceState)

            // observe navigation commands
            viewModel.navigationCommand.observe(this, Observer { navigationCommand ->
                when (navigationCommand) {
                    is NavigationCommand.To -> {
                        findNavController().navigate(navigationCommand.directions)
                    }
                    is NavigationCommand.Up -> {
                        findNavController().navigateUp()
                    }
                }
            })
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting onActivityCreated(...)")
        }
    }
    //endregion overrides
}
