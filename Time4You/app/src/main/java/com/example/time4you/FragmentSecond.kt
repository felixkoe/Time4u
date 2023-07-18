package com.example.time4you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class FragmentSecond : Fragment() {

    lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button by its ID
        val button: Button = view.findViewById(R.id.button_crate_delete_profile)
        val del_but: Button = view.findViewById(R.id.delete_db_button)
        // Set a click listener on the button
        button.setOnClickListener {
            // Handle the button click event
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentCreateProfile())
            fragmentTransaction.commit()
        }

        del_but.setOnClickListener {
            val profileDatabase = ProfileDatabase.getInstance(requireContext())
            val profileDao = profileDatabase.profileDao()
            subscribeOnBackground {
                profileDao.deleteAllProfiles()
            }
        }
    }
}
