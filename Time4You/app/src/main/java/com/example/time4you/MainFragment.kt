package com.example.time4you

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import android.os.CountDownTimer
import android.widget.TextView
import java.util.*
import android.app.TimePickerDialog


class MainFragment : Fragment() {

    interface OnFragmentBtnSelected {
        fun onButtonSelected()
    }

    interface Timer {
        fun startCountdownTimer(view: View)
    }

    lateinit var listener : OnFragmentBtnSelected
    lateinit var listener2 : Timer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val clickMe = view.findViewById<Button>(R.id.load)
        clickMe.setOnClickListener {
            listener.onButtonSelected()
        }
        val clickMe2 = view.findViewById<Button>(R.id.button)
        clickMe2.setOnClickListener {
            val countTimeView = view.findViewById<TextView>(R.id.countTime)
            listener2.startCountdownTimer(countTimeView)
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
        if(context is Timer) {
            listener2 = context
        }
        else {
            throw ClassCastException("${context.toString()} must implement")
        }
    }

}
