package com.example.time4you.view.activitys

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.MenuItem
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
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.time4you.model.ProfileRepository
import com.example.time4you.model.ProfileViewModel
import com.example.time4you.model.ProfileViewModelFactory
import androidx.lifecycle.lifecycleScope
import com.example.time4you.model.ProfileDatabase
import com.example.time4you.view.fragments.FragmentPatchNotes
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainFragment.OnFragmentBtnSelected {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var timer: CountDownTimer
    lateinit var timeDisplay: TextView
    lateinit var button: Button
    var isTimerRunning = false
    private lateinit var profileViewModel: ProfileViewModel
    var pointsToWin: Long = 0

    val profilePicturesAndIds = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.setToolbarNavigationClickListener {
            if (!isTimerRunning) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // load default Fragment

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_fragment, MainFragment())
        fragmentTransaction.commit()

        //profileViewModel.addPoints(0, 0)
        val profileRepository = ProfileRepository(application)
        val factory = ProfileViewModelFactory(profileRepository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]


    }

    @SuppressLint("SetTextI18n")
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        lifecycleScope.launch {
            profileViewModel.profile.collect { profile ->
                findViewById<TextView>(R.id.pointsall_menu).text =
                    "Points: " + profile?.pointsNow.toString()
                findViewById<TextView>(R.id.lvl_menu).text =
                    "Level: " + profile?.pointsAll.toString()
                findViewById<TextView>(R.id.profilename_menu).text =
                    profile?.firstName.toString() + " " + profile?.lastName.toString()

                val imageId = profile?.profilePic
                val selectedImageResource = profilePicturesAndIds.find { it.second == imageId }?.first
                if (selectedImageResource != null) {
                    val profileImageView = findViewById<ImageView>(R.id.imageView2)
                    profileImageView.setImageResource(selectedImageResource)
                }
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        if (menuItem.itemId == R.id.home) {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, MainFragment())
            fragmentTransaction.commit()
        }

        if (menuItem.itemId == R.id.another) {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentSecond())
            fragmentTransaction.commit()
        }

        if (menuItem.itemId == R.id.shop) {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentShop())
            fragmentTransaction.commit()
        }

        if (menuItem.itemId == R.id.patchnotes) {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_fragment, FragmentPatchNotes())
            fragmentTransaction.commit()
        }

        return true
    }


    override fun onButtonSelected() {
        if (isTimerRunning) {
            cancelTimer()
            profileViewModel.addPoints(0, 1000)
        } else {

            showTimePickerDialog()
        }

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

                if (!isAirplaneModeOn()) {
                    cancelTimer()
                    showMessage("Schalte bitte zuerst deinen Airplane Mode ein!")
                    return
                }
                pointsToWin = minutes + (hours * 60) + 1
                val showWinnablePoints = findViewById<TextView>(R.id.pointsToWin_text)
                showWinnablePoints.text = "Zu gewinnende Punkte: " + pointsToWin.toString()
                button.text = "Abbrechen"
                isTimerRunning = true
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onFinish() {
                timeDisplay.text = "00:00:00"
                var pointsToWinInt: Int = pointsToWin.toInt()
                profileViewModel.addPoints(0, pointsToWinInt)


                //timerFinished = true


                button.text = "Start"
                isTimerRunning = false
                pointsToWin = 0
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
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
        val showWinnablePoints = findViewById<TextView>(R.id.pointsToWin_text)
        showWinnablePoints.text = "Zu gewinnende Punkte: 0"
        timeDisplay.text = "00:00:00"
        button.text = "Start"
        isTimerRunning = false
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    //Ringtone

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayProfilePoints(profilePoints: Int) {
        val textPointsAll = findViewById<TextView>(R.id.pointsall_menu)
        textPointsAll.text = profilePoints.toString()
    }
}
