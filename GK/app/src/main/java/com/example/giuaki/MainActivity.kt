package com.example.giuaki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insert.setOnClickListener{


            val db = DBHelper(this, null)


            val name = Name.text.toString()
            val email = Email.text.toString()
            val contact = Contact.text.toString()
            val address = Address.text.toString()

            db.addName(name, email, contact, address)

            Toast.makeText(this, name + " added to database", Toast.LENGTH_LONG).show()

            Name.text.clear()
            Email.text.clear()
            Address.text.clear()
            Contact.text.clear()
        }


    }
}
