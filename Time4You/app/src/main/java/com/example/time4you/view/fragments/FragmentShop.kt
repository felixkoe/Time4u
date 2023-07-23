package com.example.time4you.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.time4you.R
import com.example.time4you.controller.CustomAdapter
import com.example.time4you.controller.ItemsViewModel
import com.example.time4you.model.ProfileRepository
import com.example.time4you.model.ProfileViewModel
import com.example.time4you.model.ProfileViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentShop : Fragment() {

    private var groc = 0b0000000000
    private lateinit var profileViewModel: ProfileViewModel

    private val profilePicturesAndPrices = listOf(
        Triple(R.drawable.ppic1, 50, 0b1),
        Triple(R.drawable.ppic2, 100, 0b10),
        Triple(R.drawable.ppic3, 150, 0b100),
        Triple(R.drawable.ppic4, 200, 0b1000),
        Triple(R.drawable.ppic5, 250, 0b10000),
        Triple(R.drawable.ppic6, 300, 0b100000),
        Triple(R.drawable.ppic7, 350, 0b1000000),
        Triple(R.drawable.ppic8, 400, 0b10000000),
        Triple(R.drawable.ppic9, 450, 0b100000000),
        Triple(R.drawable.ppic10, 500, 0b1000000000)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        val profileRepository = ProfileRepository(requireActivity().application)
        val factory = ProfileViewModelFactory(profileRepository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        lifecycleScope.launch {
            restoreGroc()
            withContext(Dispatchers.Main) {
                setupRecyclerView(view)
            }
        }

        return view
    }

    private suspend fun restoreGroc(){
        profileViewModel.profile.firstOrNull()?.let { profile ->
            groc = profile.level
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()


        // This loop will create 20 Views containing
        // the image with the count of view
        for ((pic, price, invNum) in profilePicturesAndPrices) {
            var displayName : String = if(checkIfBought(invNum)){
                "Bought"
            }else{
                "Costs: $price"
            }
            data.add(ItemsViewModel(pic, displayName,
                button1ClickListener = {
                    // Handle Buy click here
                    buy(price, invNum)
                    if(checkIfBought(invNum)){
                        Toast.makeText(context, "Bought", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Buy $invNum", Toast.LENGTH_SHORT).show()
                    }
                },
                button2ClickListener = {
                    // Handle Use click here
                    if(checkIfBought(invNum)){
                        replaceProfilePic(invNum)
                        Toast.makeText(requireContext(), "changed", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Not yet Bought", Toast.LENGTH_SHORT).show()
                    }
                }))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    private fun syncGroc(){
        profileViewModel.changeLevel(0, groc)
    }

    private fun checkIfBought(whichPic: Int): Boolean {
        val check = groc and whichPic
        return check > 0
    }

    private fun buy(price: Int, whichPic: Int){
        if(checkIfBought(whichPic)){
            Toast.makeText(requireContext(), "Already bought", Toast.LENGTH_SHORT).show()
        }
        else{
            profileViewModel.deletePoints(0, price)
            groc = groc or whichPic
            syncGroc()
        }
    }

    private fun replaceProfilePic(inv: Int) {
        profileViewModel.changePic(0, inv)
    }
}
