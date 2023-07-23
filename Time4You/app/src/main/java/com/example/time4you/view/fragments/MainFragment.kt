package com.example.time4you.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import java.lang.ClassCastException
import com.example.time4you.R
import com.example.time4you.model.Profile
import com.example.time4you.model.ProfileDatabase
import com.example.time4you.model.ProfileRepository
import com.example.time4you.model.ProfileViewModel
import com.example.time4you.model.ProfileViewModelFactory
import com.example.time4you.model.subscribeOnBackground
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private lateinit var listener : OnFragmentBtnSelected
    interface OnFragmentBtnSelected {
        fun onButtonSelected()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val clickMe = view.findViewById<Button>(R.id.button)
        clickMe.setOnClickListener {
            listener.onButtonSelected()
        }
        return view
    }
    override fun onAttach(context : Context){
        super.onAttach(context)

        if(context is OnFragmentBtnSelected) {
            listener = context
        }
        else {
            throw ClassCastException("$context must implement")
        }
    }

}
