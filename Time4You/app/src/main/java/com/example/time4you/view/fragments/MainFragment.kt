package com.example.time4you.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import com.example.time4you.R

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
