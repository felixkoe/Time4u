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

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var vorname: EditText
    private lateinit var nachname: EditText
    private lateinit var gender: EditText
    lateinit var vornameDb: String
    lateinit var nachnameDb: String
    lateinit var genderDb: String


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
            createProfile()
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
                vornameDb = s.toString()
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
                nachnameDb = s.toString()
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
                genderDb = s.toString()
                // Do something with the input text
            }

            override fun afterTextChanged(s: Editable?) {
                // Called after the text has changed
            }
        })
    }

    private fun createProfile(){
        val profileDatabase = ProfileDatabase.getInstance(requireContext())
        val profileDao = profileDatabase.profileDao()
        subscribeOnBackground {
            profileDao.insert(Profile(vornameDb, nachnameDb, genderDb,0,0,0,0, 0))
        }
    }
}