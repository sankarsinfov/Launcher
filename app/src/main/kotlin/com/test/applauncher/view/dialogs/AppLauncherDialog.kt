package com.test.applauncher.view.dialogs


//region import directives

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.test.applauncher.R
import com.test.applauncher.databinding.DialogAppLauncherBinding
import com.test.applauncher.service.AppLauncherFileMonitor
import com.test.applauncher.util.logging.Logger
import com.test.applauncher.view.adapters.AppLauncherAdapter
import com.test.applauncher.viewmodel.AppLauncherViewModel
import com.test.applauncher.AppLauncherDemo.Companion.context as AppContext

//endregion import directives



class AppLauncherDialog : DialogFragment() {


    private lateinit var viewModel: AppLauncherViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // request that no title be displayed
        // note: the issue of the title bar showing only appears to come into play on Lollipop and below
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // inflate the view
        val binding = DialogAppLauncherBinding.inflate(inflater, container, false)

        // set the view model
        viewModel = ViewModelProviders.of(this).get(AppLauncherViewModel::class.java)
        binding.viewmodel = viewModel

        // configure recycler view
        configureRecyclerView(binding)

        // set the fragment as the lifecycle owner of the view model
        binding.lifecycleOwner = this

        // return to caller
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        try {
            // call base class implementation
            super.onActivityCreated(savedInstanceState)

            // setup dialog animations
            // note: animates up from bottom of the screen to the center of screen on open and
            // animates down from the center of screen through the bottom of the screen on close
            dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimationUpDown

            // start the file monitoring service
            AppContext.startService(Intent(AppContext, AppLauncherFileMonitor::class.java))
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        try {
            // stop the file monitoring service
            AppLauncherFileMonitor.stopFileMonitoring()
            AppContext.stopService(Intent(AppContext, AppLauncherFileMonitor::class.java))

            // call base class implementation
            super.onDismiss(dialog)
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion DialogInterface.OnDismissListener interface implementation


    //region private methods

    /**
     * Configures our recycler view
     * @param binding Data binding reference
     */
    private fun configureRecyclerView(binding: DialogAppLauncherBinding) {
        try {
            binding.appContainer.apply {
                try {
                    // use a grid layout
                    layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.dialog_app_launcher_grid_columns))

                    // initialize our adapter
                    adapter = AppLauncherAdapter(listOf(), viewModel)

                    // start observing changes to the collection of app links that can be launched
                    viewModel.appLauncherApps.observe(this@AppLauncherDialog.viewLifecycleOwner,
                            Observer<List<AppLauncherViewModel.App>> {
                                try {
                                    // update number of columns based on number of apps (if 3 or less wide)
                                    layoutManager = if (it.size <= 3) GridLayoutManager(context, Math.max(1, it.size))
                                    else GridLayoutManager(context, resources.getInteger(R.integer.dialog_app_launcher_grid_columns))

                                    // update data set with current collection of apps
                                    (adapter as AppLauncherAdapter).apply {
                                        apps = it.sortedBy { app -> app.label.toLowerCase() }
                                        notifyDataSetChanged()
                                    }
                                } catch (ex: Exception) {
                                    Logger.w(ex)
                                }
                            })
                } catch (ex: Exception) {
                    Logger.w(ex)
                }
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion private methods
}