package com.example.finalproject

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
   var Email: EditText? = null
   var Password: EditText? = null
   var Name: EditText? = null
   var Register: Button? = null
   var NameHolder: String? = null
   var EmailHolder: String? = null
   var PasswordHolder: String? = null
   var EditTextEmptyHolder: Boolean? = null
   var sqLiteDatabaseObj: SQLiteDatabase? = null
   var SQLiteDataBaseQueryHolder: String? = null
   var sqLiteHelper: SQLiteHelper? = null
   var F_Result = "Not_Found"
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main2)
       Register = findViewById<View>(R.id.buttonRegister) as Button
       Email = findViewById<View>(R.id.editEmail) as EditText
       Password = findViewById<View>(R.id.editPassword) as EditText
       Name = findViewById<View>(R.id.editName) as EditText
       sqLiteHelper = SQLiteHelper(this)

       // Adding click listener to register button.
       Register!!.setOnClickListener { // Creating SQLite database if dose n't exists
           SQLiteDataBaseBuild()

           // Creating SQLite table if dose n't exists.
           SQLiteTableBuild()

           // Checking EditText is empty or Not.
           CheckEditTextStatus()

           // Method to check Email is already exists or not.
           CheckingEmailAlreadyExistsOrNot()

           // Empty EditText After done inserting process.
           EmptyEditTextAfterDataInsert()
       }
   }

   // SQLite database build method.
   fun SQLiteDataBaseBuild() {
       sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, MODE_PRIVATE, null)
   }

   // SQLite table build method.
   fun SQLiteTableBuild() {
       sqLiteDatabaseObj!!.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email + " VARCHAR, " + SQLiteHelper.Table_Column_3_Password + " VARCHAR);")
   }

   // Insert data into SQLite database method.
   fun InsertDataIntoSQLiteDatabase() {

       // If editText is not empty then this block will executed.
       if (EditTextEmptyHolder == true) {

           // SQLite query to insert data into table.
           SQLiteDataBaseQueryHolder =
               "INSERT INTO " + SQLiteHelper.TABLE_NAME + " (name,email,password) VALUES('" + NameHolder + "', '" + EmailHolder + "', '" + PasswordHolder + "');"

           // Executing query.
           sqLiteDatabaseObj!!.execSQL(SQLiteDataBaseQueryHolder)

           // Closing SQLite database object.
           sqLiteDatabaseObj!!.close()

           // Printing toast message after done inserting.
           Toast.makeText(this@MainActivity2, "User Registered Successfully", Toast.LENGTH_LONG)
               .show()
       } else {

           // Printing toast message if any of EditText is empty.
           Toast.makeText(
               this@MainActivity2,
               "Please Fill All The Required Fields.",
               Toast.LENGTH_LONG
           ).show()
       }
   }

   // Empty edittext after done inserting process method.
   fun EmptyEditTextAfterDataInsert() {
       Name!!.text.clear()
       Email!!.text.clear()
       Password!!.text.clear()
   }

   // Method to check EditText is empty or Not.
   fun CheckEditTextStatus() {

       // Getting value from All EditText and storing into String Variables.
       NameHolder = Name!!.text.toString()
       EmailHolder = Email!!.text.toString()
       PasswordHolder = Password!!.text.toString()
       EditTextEmptyHolder =
           if (TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(
                   PasswordHolder
               )
           ) {
               false
           } else {
               true
           }
   }

   // Checking Email is already exists or not.
   fun CheckingEmailAlreadyExistsOrNot() {
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

               // If Email is already exists then Result variable value set as Email Found.
               F_Result = "Email Found"

               // Closing cursor.
               cursor.close()
           }
       }

       // Calling method to check final result and insert data into SQLite database.
       CheckFinalResult()
   }

   // Checking result
   fun CheckFinalResult() {
       // If email already dose n't exists then user registration details will entered to SQLite database.
       InsertDataIntoSQLiteDatabase()
   }
}
