package com.pashacabu.rentateamtestapp.view.detailsScreen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pashacabu.rentateamtestapp.R
import com.pashacabu.rentateamtestapp.viewmodel.AppViewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var avatar: ImageView
    private lateinit var name: TextView
    private lateinit var lastName: TextView
    private lateinit var eMail: TextView

    private val viewModel: AppViewModel by activityViewModels<AppViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        observeViewModel()
    }

    private fun findViews(view: View) {
        avatar = view.findViewById(R.id.avatar)
        name = view.findViewById(R.id.firstName)
        lastName = view.findViewById(R.id.lastName)
        eMail = view.findViewById(R.id.email)
    }

    private fun observeViewModel() {
        viewModel.liveDetails.observe(this.viewLifecycleOwner, {
            Glide.with(requireContext())
                .load(it.avatar)
                .into(avatar)
            name.text = it.firstName
            lastName.text = it.lastName
            eMail.text = it.email
        })
    }

}