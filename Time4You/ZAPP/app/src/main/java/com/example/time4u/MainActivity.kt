package com.example.time4u


import android.app.TimePickerDialog
import android.content.ContentValues
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
* Design -> größere Nutzeroberfläche
* Patch Notes
* -> Begrüßung mit eigenem Namen
*    Store
*   mehr Profilbilder, möglichkeit einbauen zum scrollen
*
* */



/**********************************************************
******************  MAIN Aktivity / Timer  ****************
**********************************************************/

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase


    private lateinit var timer: CountDownTimer
    private lateinit var startButton: Button
    private lateinit var profileButton : Button            // Button zum erstellen eines Profils
    private lateinit var deleteProfile : Button            // Button zum löschen des Profils

    private lateinit var timeDisplay: TextView              // Timer Anzeige
    private lateinit var displayProfileName : TextView      // Anzeige Profilname
    private lateinit var displayPointsAll : TextView        // Anzeige Punkte
    private lateinit var displayProfilePicture: ImageView   // Anzeige Profilbild

    private lateinit var imageBeforeTimer : ImageView       // startendes Flugzeug
    private lateinit var imageDuringTimer : ImageView       // fliegendes Flugzeug
    private lateinit var imageAfterTimer : ImageView        // landendes  Flugzeug


    var pointsWinnable : Int = 0                            //zu holende Punkte, wenn der Timer vorbei ist

    private var isTimerRunning = false

     /*********************************************************
     *******************  **** Datenbank ****  ****************
     *********************************************************/


    class AppDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {

            internal const val DATABASE_NAME = "user_database"
            private const val DATABASE_VERSION = 1
            private const val TABLE_NAME = "users"
            private const val COLUMN_ID = "id"
            private const val COLUMN_NAME = "profileName"
            private const val COLUMN_PROFILE_IMAGE = "profileImage"
            private const val COLUMN_POINTS_ALL = "pointsAll"


            // singleton Methode
            @Volatile
            private var instance: AppDatabase? = null
            fun getInstance(context: Context): AppDatabase {
                return instance ?: synchronized(this) {
                    instance ?: AppDatabase(context.applicationContext).also { instance = it }
                }
            }
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val createTableQuery = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, profileName TEXT, profileImage TEXT, pointsALL INTEGER Default 0)"
            db?.execSQL(createTableQuery)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        fun insertUser(name: String, profileImageName: String, points: Int) {
            writableDatabase.use { db ->
                val contentValues = ContentValues().apply {
                    put(COLUMN_NAME, name)
                    put(COLUMN_PROFILE_IMAGE, profileImageName)
                    put(COLUMN_POINTS_ALL, points)
                }
                db.insert(TABLE_NAME, null, contentValues)
            }
        }
         fun deleteAllData() {
             writableDatabase.use { db ->
                 db.delete(TABLE_NAME, null, null)
             }
         }


         fun hasData(): Boolean {
             val db = readableDatabase
             val query = "SELECT COUNT(*) FROM users"
             val cursor = db.rawQuery(query, null)
             var hasData = false

             if (cursor.moveToFirst()) {
                 val count = cursor.getInt(0)
                 hasData = count > 0
             }

             cursor.close()
             db.close()

             return hasData
         }


         fun getUserName(): String? {
             var username: String? = null
             readableDatabase.use { db ->
                 val query = "SELECT profileName FROM users LIMIT 1"
                 val cursor = db.rawQuery(query, null)
                 if (cursor.moveToFirst()) {
                     val columnIndex = cursor.getColumnIndex(COLUMN_NAME)
                     if (columnIndex >= 0) {
                         username = cursor.getString(columnIndex)
                     }
                 }
                 cursor.close()
             }
             return username
         }

         fun getPointsAll(): Int {
             val db = readableDatabase
             val query = "SELECT pointsAll FROM users"
             val cursor = db.rawQuery(query, null)
             var pointsAll = 0

             if (cursor.moveToFirst()) {
                 val columnIndex = cursor.getColumnIndex(COLUMN_POINTS_ALL)
                 if (columnIndex >= 0) {
                     pointsAll = cursor.getInt(columnIndex)
                 }
             }

             cursor.close()
             db.close()

             return pointsAll
         }

         fun getProfileImage(): String? {
             val db = readableDatabase
             val query = "SELECT profileImage FROM users LIMIT 1"
             val cursor = db.rawQuery(query, null)
             var profileImage: String? = null

             if (cursor.moveToFirst()) {
                 val columnIndex = cursor.getColumnIndex(COLUMN_PROFILE_IMAGE)
                 if (columnIndex != -1) {
                     profileImage = cursor.getString(columnIndex)
                 }
             }

             cursor.close()
             db.close()

             return profileImage
         }

         fun updatePointsAll(newPointsAll: Int) {
             val db = writableDatabase
             val contentValues = ContentValues().apply {
                 put(COLUMN_POINTS_ALL, newPointsAll)
             }
             db.update(TABLE_NAME, contentValues, null, null)
             db.close()
         }



     }


    /**********************************************************
     ******************      on Create       ****************
     **********************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        db = AppDatabase.getInstance(applicationContext)

        //init variables on create
        imageBeforeTimer = findViewById(R.id.imageBeforeTimer)
        imageDuringTimer = findViewById(R.id.imageDuringTimer)
        imageAfterTimer = findViewById(R.id.imageAfterTimer)

        displayProfileName = findViewById(R.id.nutzername)
        displayPointsAll = findViewById(R.id.pointsAll)
        displayProfilePicture = findViewById(R.id.profilePicture)

        startButton = findViewById(R.id.startTimer)
        profileButton = findViewById(R.id.profile_button)
        deleteProfile = findViewById(R.id.deleteProfile)

        timeDisplay = findViewById(R.id.timeDisplay)


        //Überpürfen ob eintritt in die Main Activity über die LoginActivity
        val comingFrom = intent.getStringExtra("ComingFrom")
        if (comingFrom == "loginActivity") {
            // Code ausführen, der speziell für den Fall gilt, dass du aus der LoginAcitvity gekommen bist
            val db = AppDatabase.getInstance(this)                      // Initialisierung Variable für Datenbank
            //Get Data from loginfile
            val loginIntent = intent
            val name = loginIntent.getStringExtra("name")
            val profile = loginIntent.getStringExtra("profile")

            if (name != null && profile != null) {
                db.insertUser(name, profile, 0)
            }
            profileButton.visibility = View.GONE                                // Button für login entfernen
            deleteProfile.visibility = View.VISIBLE                             // Button für Profillöschung anzeigen lassen

            db.close()
        }

        /*********************************************************
         *******************  **** Profile ****  *****************
         *********************************************************/

        /*
        //generelles Abchecken, ob die Datenbank Daten beinhaltet
        if(db.hasData()) {
            val username = db.getUserName()             // holen des Usernamens aus der Datenbank
            val pointsAll = db.getPointsAll()           // holen des Punktestands aus der Datenbank
            if (username != null) {
                //Datenbank enthält Daten

                displayProfileName.text = username
                displayPointsAll.text = pointsAll.toString()

                if (db.getProfileImage() == "female") {
                    displayProfilePicture.setImageResource(R.drawable.frau)
                } else if (db.getProfileImage() == "male") {
                    displayProfilePicture.setImageResource(R.drawable.mann)
                }


                profileButton.visibility = View.GONE
                deleteProfile.visibility = View.VISIBLE
            }
            }else {
                //Datenbank enthält keine Daten
                profileButton.visibility = View.VISIBLE
                deleteProfile.visibility = View.GONE
        }
        */

        checkDatabase(db, displayProfileName, displayPointsAll, displayProfilePicture, profileButton, deleteProfile)

        /*********************************************************
         *******************  **** Buttons  ****  ****************
         *********************************************************/

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

        profileButton.setOnClickListener {                          //Überprüfe ob User bereits angelegt

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        deleteProfile.setOnClickListener {
            db.deleteAllData()

            checkDatabase(db, displayProfileName, displayPointsAll, displayProfilePicture, profileButton, deleteProfile)
            profileButton.visibility = View.VISIBLE
            deleteProfile.visibility = View.GONE
        }
    }

    /*********************************************************
     *****************  ****  Funktions ****  ****************
     *********************************************************/


    override fun onDestroy() {
        super.onDestroy()
        db.close()
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
                // zu Holende Punkte mit den aktuell bestehenden Punkten von der Datenbank addieren und in der DB abspeichern
                db.updatePointsAll(pointsWinnable)

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

//Show Message on Screen
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun checkDatabase(db: AppDatabase, usernameView: TextView, pointsAllView: TextView, profileImageView: ImageView, profileButton: Button, deleteProfile: Button) {
        if (db.hasData()) {
            val username = db.getUserName()
            val pointsAll = db.getPointsAll()

            if (username != null) {
                // Datenbank enthält Daten
                usernameView.text = username
                pointsAllView.text = pointsAll.toString()

                if (db.getProfileImage() == "female") {
                    profileImageView.setImageResource(R.drawable.mann)
                } else if (db.getProfileImage() == "male") {
                    profileImageView.setImageResource(R.drawable.frau)
                }

                profileButton.visibility = View.GONE
                deleteProfile.visibility = View.VISIBLE
            }
        } else {
            // Datenbank enthält keine Daten / Daten wurden gelöscht
            usernameView.text = ""
            profileImageView.setImageResource(0)
            pointsAllView.visibility = View.GONE
            profileButton.visibility = View.VISIBLE
            deleteProfile.visibility = View.GONE
        }
    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Füge das Menülayout hinzu
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle Aktionen basierend auf der ausgewählten Option
        when (item.itemId) {
            R.id.action_settings -> {
                // Hier kannst du den Code für die Einstellungen einfügen
                return true
            }
            // Füge hier weitere Optionen hinzu, falls gewünscht
            else -> return super.onOptionsItemSelected(item)
        }
    }

*/

}






