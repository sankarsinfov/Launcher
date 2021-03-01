package com.test.applauncher.view.adapters


//region import directives

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.applauncher.R
import com.test.applauncher.event.interfaces.AppLauncherCallback
import com.test.applauncher.util.logging.Logger
import com.test.applauncher.viewmodel.AppLauncherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_app.view.*

//endregion import directives



class AppLauncherAdapter(var apps: List<AppLauncherViewModel.App>,
                         private val clickCallback: AppLauncherCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //region inner classes

    /**
     * AppLauncher ViewHolder
     */
    inner class AppLauncherViewHolder(val view: View,
                                      private val clickCallback: AppLauncherCallback) : RecyclerView.ViewHolder(view) {
        fun bind(app: AppLauncherViewModel.App) {
            try {
                // first set the icon to null (this fixes the issue where the wrong image sometimes displays due to recycling behavior)
                view.app_icon.setImageDrawable(null)

                when (app) {
                    is AppLauncherViewModel.App.AppInfo -> {
                        // app card
                        view.app_card.setOnClickListener { clickCallback.onAppClicked(app) }

                        // app icon
                        view.app_icon.setImageDrawable(app.icon)

                        // app text
                        view.app_text.text = app.label
                    }
                }

                // required for marquee effect on long strings
                view.app_text.isSelected = true
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppLauncherViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.card_app, parent, false), clickCallback)
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        try {
            (viewHolder as AppLauncherViewHolder).bind(apps[position])
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = apps.size

    /**
     * Called when a view created by this adapter has been recycled.
     *
     * @param holder The ViewHolder for the view being recycled
     */
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        try {
            // call base class implementation
            super.onViewRecycled(holder)

            // cancel any pending requests to load an image
            Picasso.get().cancelRequest((holder as AppLauncherViewHolder).view.app_icon)
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion overrides
}