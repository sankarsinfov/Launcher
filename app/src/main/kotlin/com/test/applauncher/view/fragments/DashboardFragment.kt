package com.test.applauncher.view.fragments


//region import directives

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.test.applauncher.databinding.FragmentDashboardBinding
import com.test.applauncher.viewmodel.DashboardViewModel

//endregion import directives


class DashboardFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate the view
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // set the view model
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        binding.viewmodel = viewModel as DashboardViewModel

        // set the fragment as the lifecycle owner of the view model
        binding.lifecycleOwner = this

        // return to caller
        return binding.root
    }
    //endregion lifecycle overrides
}
