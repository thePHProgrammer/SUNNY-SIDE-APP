package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.start)

       val buttonClick = findViewById<Button>(R.id.startBtn)
       buttonClick.setOnClickListener{
           val intent = Intent(this, hello::class.java)
           startActivity(intent)
       }
   }
}
