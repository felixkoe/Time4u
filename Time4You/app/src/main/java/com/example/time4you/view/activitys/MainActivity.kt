package com.example.time4you.view.activitys

import android.app.TimePickerDialog
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
import com.example.time4you.R
import com.example.time4you.view.fragments.MainFragment
import com.example.time4you.view.fragments.FragmentSecond
import com.example.time4you.view.fragments.FragmentShop
import com.google.android.material.navigation.NavigationView
import java.util.Calendar
import android.media.RingtoneManager
import android.widget.Button
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainFragment.OnFragmentBtnSelected{

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var fragmentManager : FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var timer: CountDownTimer
    lateinit var timeDisplay: TextView
    lateinit var button: Button
    var isTimerRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )
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

    override fun onButtonSelected() {
        showTimePickerDialog()
       /* fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_fragment, FragmentSecond())
        fragmentTransaction.commit()
        */

    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTimeInMillis = (selectedHour * 3600 + selectedMinute * 60) * 1000L
            startTimer(selectedTimeInMillis)

         //   pointsWinnable = selectedHour *60 + selectedMinute + 1 //mögliche zu erzielende Punkte errechnen (1 Minute = 1 Punkt)
        }, currentHour, currentMinute, true)


        timePickerDialog.show()
    }

    //Timer
    private fun startTimer(totalTimeMillis: Long) {
        timeDisplay = findViewById(R.id.timeDisplay)
        button = findViewById(R.id.button)
        timer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val hours = secondsRemaining / 3600
                val minutes = (secondsRemaining % 3600) / 60
                val seconds = secondsRemaining % 60

                val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                //zu gewinnende Punkte anzeigen lassen:
                //val showWinnablePoints = findViewById<TextView>(R.id.showWinnablePoints)
                //showWinnablePoints.text = pointsWinnable.toString()

                timeDisplay.text = timeString

                if(!isAirplaneModeOn())
                {
                    cancelTimer()
                    return
                }

                button.text = "Abbrechen"
                isTimerRunning = true
            }

            override fun onFinish() {
                timeDisplay.text = "00:00:00"
                playRingtone()
                // zu Holende Punkte mit den aktuell bestehenden Punkten von der Datenbank addieren und in der DB abspeichern


                //db.updatePointsAll(pointsWinnable)
                //timerFinished = true
                //checkDatabase(db, displayProfileName, displayPointsAll, displayProfilePicture, profileButton, deleteProfile)

                button.text = "Start"
                isTimerRunning = false
            }
        }

        timer.start()

       // imageBeforeTimer.visibility = View.INVISIBLE                        //Bilder aktualisieren
       // imageDuringTimer.visibility = View.VISIBLE
        button.isEnabled = true
    }

    private fun cancelTimer() {
        timer.cancel()
        showMessage("Der Timer wurde abgebrochen, es werden keine Punkte gutgeschrieben!")
      //  imageDuringTimer.visibility = View.INVISIBLE
     //   imageAfterTimer.visibility = View.VISIBLE

        timeDisplay.text = "00:00:00"
        button.text = "Start"
        isTimerRunning = false
    }

    //Ringtone
    private fun playRingtone() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        ringtone.play()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}