package com.example.time4u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.EditText
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.CheckBox

class WelcomeActivity : AppCompatActivity() {
    private lateinit var profileName: EditText
    private lateinit var btnSelectImage: CheckBox

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        profileName = findViewById(R.id.profileName)
        btnSelectImage = findViewById(R.id.btnSelectImage)

        databaseHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val isFirstTime = sharedPreferences.getBoolean("is_first_time", true)

        if (isFirstTime) {
            // Der Benutzer öffnet die App zum ersten Mal
            showWelcomeView()
        } else {
            // Der Benutzer hat die Willkommensansicht bereits besucht
            goToMainActivity()
        }
    }

    private fun showWelcomeView() {
        // Zeige die Willkommensansicht an und ermögliche die Eingabe von Namen und Auswahl des Profilbilds

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            // Speichere den Namen in der Datenbank
            databaseHelper.saveName(name)

            // Speichere den Zustand, dass der Benutzer die Willkommensansicht bereits besucht hat
            sharedPreferences.edit().putBoolean("is_first_time", false).apply()

            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        // Starte die Haupt-Activity und beende die Willkommens-Activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}