package com.example.time4you.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.time4you.R
import com.example.time4you.controller.CustomAdapter
import com.example.time4you.model.ItemsViewModel
import com.example.time4you.model.ProfileRepository
import com.example.time4you.model.ProfileViewModel
import com.example.time4you.model.ProfileViewModelFactory
import com.example.time4you.model.Quadruple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class FragmentShop : Fragment() {

    private var boughtPics = listOf(
        Triple(R.drawable.ppic1, 0b1, 0),
        Triple(R.drawable.ppic2, 0b10, 0),
        Triple(R.drawable.ppic3, 0b100, 0),
        Triple(R.drawable.ppic4, 0b1000, 0),
        Triple(R.drawable.ppic5, 0b10000, 0),
        Triple(R.drawable.ppic6, 0b100000, 0),
        Triple(R.drawable.ppic7, 0b1000000, 0),
        Triple(R.drawable.ppic8, 0b10000000, 0),
        Triple(R.drawable.ppic9, 0b100000000, 0),
        Triple(R.drawable.ppic10, 0b1000000000, 0)
    )
   var  newBoughtPics = boughtPics.toMutableList()

    private var groc: Int = 0b0000000000
    private lateinit var profileViewModel: ProfileViewModel
    private var levelLocal : Int = 0
    private var pointsNow : Int = 0


    private val profilePicturesAndPrices: List<Quadruple<Int, Int, Int, Int>> = listOf(
        Quadruple(R.drawable.ppic1, 50, 0b1, 50),
        Quadruple(R.drawable.ppic2, 100, 0b10, 50),
        Quadruple(R.drawable.ppic3, 150, 0b100, 100),
        Quadruple(R.drawable.ppic4, 200, 0b1000, 100),
        Quadruple(R.drawable.ppic5, 250, 0b10000, 500),
        Quadruple(R.drawable.ppic6, 300, 0b100000, 500),
        Quadruple(R.drawable.ppic7, 350, 0b1000000, 1000),
        Quadruple(R.drawable.ppic8, 400, 0b10000000, 1000),
        Quadruple(R.drawable.ppic9, 450, 0b100000000, 2000),
        Quadruple(R.drawable.ppic10, 500, 0b1000000000, 2000)
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
            istBoughtAll()
            withContext(Dispatchers.Main) {
                restoreLevel()
                getPoints()
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

    private suspend fun restoreLevel(){
        profileViewModel.profile.firstOrNull()?.let { profile ->
            levelLocal = profile.pointsAll
        }
    }

    private suspend fun getPoints(){
        profileViewModel.profile.firstOrNull()?.let { profile ->
            pointsNow = profile.pointsNow
        }
    }
    private fun setupRecyclerView(view: View) {
        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()
        // This loop will create 10 Views containing
        // the image with the count of view
        for (i in profilePicturesAndPrices.indices) {
            val (pic, price, invNum, lvl) = profilePicturesAndPrices[i]
            val (_, _, isbought) = newBoughtPics[i]
            val displayName : String = if(isbought == 1){
                "Bought"
            }else{
                "Costs: $price"
            }
            data.add(
                ItemsViewModel(pic, displayName,
                    button1ClickListener = {
                        // Handle Buy click here
                        buy(price, invNum, lvl)
                        refreshFragment()
                    },
                    button2ClickListener = {
                        // Handle Use click here
                        if(checkIfBought(invNum)){
                            replaceProfilePic(invNum)
                            Toast.makeText(requireContext(), "changed", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Not yet Bought", Toast.LENGTH_SHORT).show()
                        }
                    }, "At Lvl: $lvl")  // Pass 'text2' as the last parameter
            )
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

    private fun buy(price: Int, whichPic: Int, levelPic: Int){
        if(checkIfBought(whichPic)){
            Toast.makeText(requireContext(), "Already bought", Toast.LENGTH_SHORT).show()
        }
        else{
            if(price <= pointsNow && levelPic <= levelLocal){
                profileViewModel.deletePoints(0, price)
                groc = groc or whichPic
                Toast.makeText(requireContext(), "Bought", Toast.LENGTH_SHORT).show()
                syncGroc()}
            else{
                Toast.makeText(requireContext(), "Not Enough Points or lvl not high enough!! $pointsNow", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun istBoughtAll(){
        for (i in newBoughtPics.indices) {
            val (pic, inv, isbought) = newBoughtPics[i]
            if ((groc and inv) == inv) {
                newBoughtPics[i] = Triple(pic, inv, 1)
            } else {
                newBoughtPics[i] = Triple(pic, inv, 0)
            }
        }
    }
    private fun replaceProfilePic(inv: Int) {
        profileViewModel.changePic(0, inv)
    }

    private fun refreshFragment() {
        val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()

        // Replace the current instance of this fragment with a new instance
        fragmentTransaction.replace(R.id.container_fragment, FragmentShop())

        // Commit the transaction
        fragmentTransaction.commit()
    }
}
