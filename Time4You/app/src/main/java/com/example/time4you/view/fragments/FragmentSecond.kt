package com.example.time4you.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.time4you.model.ProfileDatabase
import com.example.time4you.R
import com.example.time4you.model.ProfileRepository
import com.example.time4you.model.ProfileViewModel
import com.example.time4you.model.ProfileViewModelFactory
import com.example.time4you.controller.subscribeOnBackground
import kotlinx.coroutines.launch

class FragmentSecond : Fragment() {

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var profileViewModel: ProfileViewModel

    private val profilePicturesAndIds = listOf(
        Pair(R.drawable.ppic1, 0b1),
        Pair(R.drawable.ppic2, 0b10),
        Pair(R.drawable.ppic3, 0b100),
        Pair(R.drawable.ppic4, 0b1000),
        Pair(R.drawable.ppic5, 0b10000),
        Pair(R.drawable.ppic6, 0b100000),
        Pair(R.drawable.ppic7, 0b1000000),
        Pair(R.drawable.ppic8, 0b10000000),
        Pair(R.drawable.ppic9, 0b100000000),
        Pair(R.drawable.ppic10, 0b1000000000)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val profileRepository = ProfileRepository(requireActivity().application)
        val factory = ProfileViewModelFactory(profileRepository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileViewModel.profile.collect { profile ->
                view.findViewById<TextView>(R.id.points).text = "Points: " + profile?.pointsNow.toString()
                view.findViewById<TextView>(R.id.lvl)?.text = "Level: " + profile?.pointsAll.toString()
                view.findViewById<TextView>(R.id.profile_name).text = profile?.firstName.toString() + " " + profile?.lastName.toString()

                val imageId = profile?.profilePic
                val selectedImageResource = profilePicturesAndIds.find { it.second == imageId }?.first
                if (selectedImageResource != null) {
                    val profileImageView = view.findViewById<ImageView>(R.id.imageView7)
                    profileImageView.setImageResource(selectedImageResource)
                }
            }
        }

        // Find the button by its ID
        val button: Button = view.findViewById(R.id.button_crate_delete_profile)
        val delBut: Button = view.findViewById(R.id.delete_db_button)

        // Set a click listener on the button
        button.setOnClickListener {
            // Handle the button click event
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentCreateProfile())
            fragmentTransaction.commit()
        }

        delBut.setOnClickListener {
            val profileDatabase = ProfileDatabase.getInstance(requireContext())
            val profileDao = profileDatabase.profileDao()
            subscribeOnBackground {
                profileDao.deleteAllProfiles()
            }
        }
    }
}
