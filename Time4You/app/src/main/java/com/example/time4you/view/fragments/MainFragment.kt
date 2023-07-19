package com.example.time4you.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import com.example.time4you.model.ProfileDatabase
import com.example.time4you.R


class MainFragment : Fragment() {

    interface OnFragmentBtnSelected {
        fun onButtonSelected()
    }


    lateinit var listener : OnFragmentBtnSelected

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

        val profileDatabase = ProfileDatabase.getInstance(requireContext())
        //    ProfileDatabase.deleteDatabase(profileDatabase)
        //    ProfileDatabase.populateDatabase(profileDatabase)

        return view
    }

    override fun onAttach(context : Context){
        super.onAttach(context)
        if(context is OnFragmentBtnSelected) {
            listener = context
        }
        else {
            throw ClassCastException("${context.toString()} must implement")
        }
    }

}
