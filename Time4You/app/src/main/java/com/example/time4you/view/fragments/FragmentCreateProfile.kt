package com.example.time4you.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.time4you.model.Profile
import com.example.time4you.model.ProfileDatabase
import com.example.time4you.R
import com.example.time4you.model.subscribeOnBackground

class FragmentCreateProfile : Fragment() {

    lateinit var fragmentTransaction: FragmentTransaction;
    lateinit var vorname: EditText
    lateinit var nachname: EditText
    lateinit var gender: EditText

    lateinit var vorname_db: String
    lateinit var nachname_db: String
    lateinit var gender_db: String


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_profile, container, false)
        vorname = view.findViewById(R.id.vorname_edit)
        nachname = view.findViewById(R.id.nachname_edit)
        gender = view.findViewById(R.id.gender_edit)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button by its ID
        val button: Button = view.findViewById(R.id.buttin_create_profile)

        // Set a click listener on the button
        button.setOnClickListener {
            // Handle the button click event
            create_profile()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentSecond())
            fragmentTransaction.commit()
        }

        // Set a listener to handle text input
        vorname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                vorname_db = s.toString()
                // Do something with the input text
            }

            override fun afterTextChanged(s: Editable?) {
                // Called after the text has changed
            }
        })

        nachname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                nachname_db = s.toString()
                // Do something with the input text
            }

            override fun afterTextChanged(s: Editable?) {
                // Called after the text has changed
            }
        })

        gender.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                gender_db = s.toString()
                // Do something with the input text
            }

            override fun afterTextChanged(s: Editable?) {
                // Called after the text has changed
            }
        })
    }

    fun create_profile(){
        val profileDatabase = ProfileDatabase.getInstance(requireContext())

        val profileDao = profileDatabase.profileDao()
        subscribeOnBackground {
            profileDao.insert(Profile(vorname_db, nachname_db, gender_db,0,0,0,0))
        }
    }
}