package com.test.applauncher.service


//region import directives

import android.app.Service
import android.content.Intent
import android.os.FileObserver
import com.test.applauncher.constants.APP_LAUNCHER_CONFIGURATION_FILE
import com.test.applauncher.event.AppLauncherEvents
import com.test.applauncher.util.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicBoolean
import com.test.applauncher.AppLauncherDemo.Companion.context as AppContext


class AppLauncherFileMonitor : Service() {

    //region companion object
    companion object {

        /**
         * Used to monitor for AppLauncher configuration changes via a config file
         */
        private val fileObserver = AppLauncherFileObserver()

        /**
         * Used to stop monitoring of the App Launcher configuration file
         */
        fun stopFileMonitoring() {
            // log entry
            Logger.i("Entered stopFileMonitoring()")

            try {
                if (fileObserver.observing.compareAndSet(true, false)) {
                    Logger.i("Stopping AppLauncher file monitoring")
                    fileObserver.observer.stopWatching()
                } else {
                    Logger.i("Skipping stopping AppLauncher file monitoring, we do not appear to monitoring at this time")
                }
            } catch (ex: Exception) {
                Logger.w(ex)
            } finally {
                // log exit
                Logger.i("Exiting stopFileMonitoring()")
            }
        }
    }
    //endregion companion object


    //region Service implementation

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    override fun onCreate() {
        try {
            // log service created
            Logger.i("Creating AppLauncherFileMonitor service")

            // call base class implementation
            super.onCreate()
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // log entry
        Logger.i("Entered onStartCommand()")

        try {
            if (fileObserver.observing.compareAndSet(false, true)) {
                Logger.i("Starting AppLauncher file monitoring")
                CoroutineScope(Dispatchers.Default).launch {
                    observeAppLauncherFileConfiguration()
                }
            } else {
                Logger.i("Skipping starting AppLauncher file monitoring, we appear to already be monitoring")
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting onStartCommand()")
        }

        // do not recreate the service unless there are pending intents to deliver
        return START_NOT_STICKY
    }

    /**
     * Return the communication channel to the service.  May return null if clients can not bind
     * to the service.
     *
     * @param intent The Intent that was used to bind to this service. Note that any extras that
     * were included with the Intent at that point will <em>not</em> be seen here.
     *
     * @return Return an IBinder through which clients can call on to the service.
     */
    override fun onBind(intent: Intent?) = null
    //endregion Service implementation


    //region inner classes

    /**
     * Wrapper around a late init FileObserver
     */
    private class AppLauncherFileObserver {

        //region data members

        /**
         * Our file observer that we use to monitor the AppLauncher configuration file
         */
        lateinit var observer: FileObserver

        /**
         * Flag indicating whether or not we're currently observing file-based configuration
         */
        val observing = AtomicBoolean(false)
        //endregion data members
    }
    //endregion inner classes


    //region private methods

    /**
     * Observes changes to the AppLauncher file configuration
     */
    private fun observeAppLauncherFileConfiguration() {
        // log entry
        Logger.i("Entered observeAppLauncherFileConfiguration()")

        try {
            fileObserver.observer = object : FileObserver(AppContext.getExternalFilesDir(null)?.absolutePath, ALL_EVENTS) {
                override fun onEvent(event: Int, path: String?) {
                    try {
                        if (path?.equals(APP_LAUNCHER_CONFIGURATION_FILE, ignoreCase = true) == true) {
                            when (event) {
                                CREATE -> {
                                    // notify that the file configuration has been created / modified
                                    Logger.i("AppLauncher file based configuration monitoring event received (CREATE)")
                                    EventBus.getDefault().post(AppLauncherEvents.AppLauncherFileConfigurationCreatedEvent)
                                }

                                MODIFY -> {
                                    // notify that the file configuration has been created / modified
                                    Logger.i("AppLauncher file based configuration monitoring event received (MODIFY)")
                                    EventBus.getDefault().post(AppLauncherEvents.AppLauncherFileConfigurationModifiedEvent)
                                }

                                DELETE -> {
                                    // notify that the file configuration has been removed
                                    Logger.i("AppLauncher file based configuration monitoring event received (DELETE)")
                                    EventBus.getDefault().post(AppLauncherEvents.AppLauncherFileConfigurationDeletedEvent)
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        Logger.w(ex)
                    }
                }
            }
            fileObserver.observer.startWatching()
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting observeAppLauncherFileConfiguration()")
        }
    }
    //endregion private methods
}