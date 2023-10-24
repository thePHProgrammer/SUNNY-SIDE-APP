package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class hello : AppCompatActivity() {
   var Email: EditText? = null
   var Password: EditText? = null
   var EmailHolder: String? = null
   var PasswordHolder: String? = null
   var EditTextEmptyHolder: Boolean? = null
   var sqLiteDatabaseObj: SQLiteDatabase? = null
   var sqLiteHelper: SQLiteHelper? = null
   var TempPassword = "NOT_FOUND"
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
       val LogInButton = findViewById<Button>(R.id.buttonLogin)

       Email = findViewById<View>(R.id.editEmail) as EditText
       Password = findViewById<View>(R.id.editPassword) as EditText
       sqLiteHelper = SQLiteHelper(this)

       LogInButton!!.setOnClickListener {
           CheckEditTextStatus()
           LoginFunction()
       }

       // Adding click listener to register button.
       val buttonClick = findViewById<Button>(R.id.buttonRegister)
       buttonClick.setOnClickListener{
           val intent = Intent(this@hello, MainActivity2::class.java)
           startActivity(intent)
       }
   }

   // Login function starts from here.
   @SuppressLint("Range")
   fun LoginFunction() {
       if (EditTextEmptyHolder!!) {

           // Opening SQLite database write permission.
           sqLiteDatabaseObj = sqLiteHelper!!.getWritableDatabase()

           // Adding search email query to cursor.
           val cursor = sqLiteDatabaseObj!!.query(
               SQLiteHelper.TABLE_NAME,
               null,
               " " + SQLiteHelper.Table_Column_2_Email.toString() + "=?",
               arrayOf(EmailHolder),
               null,
               null,
               null
           )
           while (cursor.moveToNext()) {
               if (cursor.isFirst()) {
                   cursor.moveToFirst()

                   // Storing Password associated with entered email.
                   TempPassword =
                       cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password))

                   // Closing cursor.
                   cursor.close()
               }
           }

           // Calling method to check final result ..
           CheckFinalResult()
       } else {

           //If any of login EditText empty then this block will be executed.
           Toast.makeText(
               this@hello,
               "Please Enter UserName or Password.",
               Toast.LENGTH_LONG
           ).show()
       }
   }

   // Checking EditText is empty or not.
   fun CheckEditTextStatus() {

       // Getting value from All EditText and storing into String Variables.
       EmailHolder = Email!!.text.toString()
       PasswordHolder = Password!!.text.toString()

       // Checking EditText is empty or no using TextUtils.
       EditTextEmptyHolder =
           if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
               false
           } else {
               true
           }
   }

   // Checking entered password from SQLite database email associated password.
   fun CheckFinalResult() {
       if (TempPassword.equals(PasswordHolder, ignoreCase = true)) {
           Toast.makeText(this@hello, "Login Successful", Toast.LENGTH_LONG).show()
           val intent = Intent(this@hello, DashboardActivity::class.java)

           intent.putExtra(UserEmail, EmailHolder)
           startActivity(intent)
       } else {
           Toast.makeText(
               this@hello,
               "UserName or Password is Wrong, Please Try Again.",
               Toast.LENGTH_LONG
           ).show()
       }
       TempPassword = "NOT_FOUND"
   }

   companion object {
       const val UserEmail = ""
   }
}
