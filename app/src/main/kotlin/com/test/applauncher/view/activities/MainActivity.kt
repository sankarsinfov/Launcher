package com.test.applauncher.view.activities


//region import directives

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.applauncher.R
import com.test.applauncher.util.logging.Logger


class MainActivity : AppCompatActivity() {

    //region lifecycle overrides

    /**
     * Called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            // call base class implementation
            super.onCreate(savedInstanceState)

            // inflate layout
            setContentView(R.layout.activity_main)
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion lifecycle overrides
}
