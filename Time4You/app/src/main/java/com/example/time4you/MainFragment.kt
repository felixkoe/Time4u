package com.example.time4you

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class MainFragment : Fragment() {
    lateinit var listener : onFragmentBtnSelected

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
        return view
    }

    override fun onAttach(context : Context){
        super.onAttach(context)
        if(context is onFragmentBtnSelected) {
            listener = context as onFragmentBtnSelected
        }
        else {
            throw ClassCastException("${context.toString()} must implement OnFragmentBtnSelected")
        }


    }

    interface onFragmentBtnSelected{
        fun onButtonSelected();
    }



}


/*
public void onAttach(@NoNull Context context){
    super.onAttach(context);
    if(context is onFragmentBtnSelected) {
        listener = (onFragmentBtnSelected) context;
     }
     else {
            throw ClassCastException(context.toString()) + " must implement listener";
      }


    }
 */