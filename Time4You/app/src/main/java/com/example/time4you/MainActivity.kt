package com.example.time4you

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentBtnSelected, MainFragment.Timer {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var fragmentManager : FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()

        // load default Fragment

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_fragment, MainFragment())
        fragmentTransaction.commit()


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        if(menuItem.itemId == R.id.home){
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, MainFragment())
            fragmentTransaction.commit()
        }

        if(menuItem.itemId == R.id.another){
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentSecond())
            fragmentTransaction.commit()
        }

        if(menuItem.itemId == R.id.shop){
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentShop())
            fragmentTransaction.commit()
        }

        return true
    }
    override fun startCountdownTimer(view: View) {
        showTimePickerDialog()
        val countTime: TextView = view.findViewById(R.id.countTime)
        var countDownTimer = object : CountDownTimer(50000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                countTime.text = secondsLeft.toString()
            }

            override fun onFinish() {
                countTime.text = "Finished"
            }
        }.start()
    }

    override fun onButtonSelected() {
        var test = isAirplaneModeOn()
        Toast.makeText(this@MainActivity, "Der Flugmodus ist auf var: $test", Toast.LENGTH_SHORT).show()
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_fragment, FragmentSecond())
        fragmentTransaction.commit()


    }

    fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTimeInMillis = (selectedHour * 3600 + selectedMinute * 60) * 1000L
            // startTimer(selectedTimeInMillis)

            // pointsWinnable = selectedHour *60 + selectedMinute + 1 //mögliche zu erzielende Punkte errechnen (1 Minute = 1 Punkt)
        }, currentHour, currentMinute, true)


        timePickerDialog.show()
    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }
}