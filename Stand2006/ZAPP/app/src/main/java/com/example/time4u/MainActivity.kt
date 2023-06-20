package com.example.time4u


import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
/*
* TO DO
*
* Design
* Datenbank einbinden
* einstellungen -> App zurücksetzen / Daten löschen
* Patch Notes
*
* Namen eingeben, beim ersten login -> in Datenbank hinterlegen
* -> Begrüßung mit eigenem Namen
*
* Profil erstellen
* */



/**********************************************************
******************  MAIN Aktivity / Timer  ****************
**********************************************************/

class MainActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private lateinit var startButton: Button
    private lateinit var timeDisplay: TextView              //Timer Anzeige
    private lateinit var imageBeforeTimer : ImageView       //startendes Flugzeug
    private lateinit var imageDuringTimer : ImageView       //fliegendes Flugzeug
    private lateinit var imageAfterTimer : ImageView        //landendes  Flugzeug

    var pointsAll : Int = 0                                 //gesamte Punkte
    var pointsWinnable : Int = 0                            //zu holende Punkte, wenn der Timer vorbei ist

    private var isTimerRunning = false
    private var isAirplaneModeEnabled = false

    /**********************************************************
     *******************  **** Datenbank ****  ****************
     **********************************************************/

    class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object {
            private const val DATABASE_NAME = "mydatabase.db"
            private const val DATABASE_VERSION = 1
            private const val TABLE_NAME = "user"
            private const val COLUMN_NAME = "name"
            private const val COLUMN_SCORE = "score"
        }

        override fun onCreate(db: SQLiteDatabase) {
            // Erstelle die Tabellen und füge Initialdaten hinzu
            db.execSQL("CREATE TABLE IF NOT EXISTS mytable (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Aktualisierungslogik bei Bedarf
        }
    }
    /**********************************************************/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init variables on create
        imageBeforeTimer = findViewById(R.id.imageBeforeTimer)
        imageDuringTimer = findViewById(R.id.imageDuringTimer)
        imageAfterTimer = findViewById(R.id.imageAfterTimer)

        startButton = findViewById(R.id.startTimer)
        timeDisplay = findViewById(R.id.timeDisplay)

        startButton.setOnClickListener {
            if (isAirplaneModeOn()) {
                if (isTimerRunning) {
                    cancelTimer()
                } else {
                    showTimePickerDialog()
                }
            } else {
                showMessage("Flugzeugmodus ist nicht aktiviert")
                openAirplaneModeSettings()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    private fun openAirplaneModeSettings() {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        startActivity(intent)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTimeInMillis = (selectedHour * 3600 + selectedMinute * 60) * 1000L
            startTimer(selectedTimeInMillis)
            pointsWinnable = selectedHour *60 + selectedMinute  //mögliche zu erzielende Punkte errechnen
        }, currentHour, currentMinute, true)


        timePickerDialog.show()
    }

//Timer
    private fun startTimer(totalTimeMillis: Long) {
        timer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val hours = secondsRemaining / 3600
                val minutes = (secondsRemaining % 3600) / 60
                val seconds = secondsRemaining % 60

                val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                //zu gewinnende Punkte anzeigen lassen:
                val showWinnablePoints = findViewById<TextView>(R.id.showWinnablePoints)
                showWinnablePoints.text = pointsWinnable.toString()


                timeDisplay.text = timeString

                if(!isAirplaneModeOn())
                {
                    cancelTimer()
                    return
                }

                startButton.text = "Abbrechen"
                isTimerRunning = true
            }

            override fun onFinish() {
                timeDisplay.text = "00:00:00"
                playRingtone()
                startButton.text = "Start"
                isTimerRunning = false
            }
        }

        timer.start()

        imageBeforeTimer.visibility = View.INVISIBLE                        //Bilder aktualisieren
        imageDuringTimer.visibility = View.VISIBLE
        startButton.isEnabled = true
    }

    private fun cancelTimer() {
        timer.cancel()
        showMessage("Der Timer wurder abgebrochen, es werden keine Punkte gutgeschrieben!")
        imageDuringTimer.visibility = View.INVISIBLE
        imageAfterTimer.visibility = View.VISIBLE

        timeDisplay.text = "00:00:00"
        startButton.text = "Start"
        isTimerRunning = false
    }

//Ringtone
    private fun playRingtone() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        ringtone.play()
    }

//Show Message on Screem
    private fun showMessage(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}



