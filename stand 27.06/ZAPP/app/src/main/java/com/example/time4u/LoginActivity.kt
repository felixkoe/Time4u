package com.example.time4u


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val checkboxFemale = findViewById<CheckBox>(R.id.checkbox_female)
        val checkboxMale = findViewById<CheckBox>(R.id.checkbox_male)

        val buttonSave = findViewById<Button>(R.id.buttonWeiter)

        buttonSave.setOnClickListener {
            val nameEditText = findViewById<EditText>(R.id.profileName)
            val name = nameEditText.text.toString()
            val profile : String = if (checkboxFemale.isChecked) {
                "female"
            }else{
                "male"
            }

            if (name.isNotEmpty() && (checkboxFemale.isChecked || checkboxMale.isChecked)) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("profile", profile)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Bitte gib einen Profilnamen ein und wähle mindestens eine Checkbox aus.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}




