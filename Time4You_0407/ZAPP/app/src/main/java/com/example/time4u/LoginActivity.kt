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

        //Sicherstellen, dass nur eine Checkbox gewähält wird
        checkboxFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Checkbox 1 wurde ausgewählt
                checkboxMale.isChecked = false // Checkbox 2 abwählen
            }
        }
        checkboxMale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Checkbox 2 wurde ausgewählt
                checkboxFemale.isChecked = false // Checkbox 1 abwählen
            }
        }

        buttonSave.setOnClickListener {
            val nameEditText = findViewById<EditText>(R.id.profileName)
            val name = nameEditText.text.toString()
            val profile : String = if (checkboxFemale.isChecked) {
                "female"
            }else{
                "male"
            }

            if (name.isNotEmpty() && (checkboxFemale.isChecked || checkboxMale.isChecked)) {
                val loginIntent = Intent(this, MainActivity::class.java)
                loginIntent.putExtra("name", name)
                loginIntent.putExtra("profile", profile)
                loginIntent.putExtra("ComingFrom", "loginActivity")
                startActivity(loginIntent)
                finish()
            } else {
                Toast.makeText(this, "Bitte gib einen Profilnamen ein und wähle mindestens eine Checkbox aus.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}




