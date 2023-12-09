package com.bangkit.nanaspos.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.jakewharton.threetenabp.AndroidThreeTen
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun getDateTime(s: String): String? {
    try {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            // Replace this timestamp string with your actual timestamp...
            val timestampString = s

            // Define the input date-time format pattern
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

            // Parse the timestamp string into a LocalDateTime object
            val localDateTime = LocalDateTime.parse(timestampString, inputFormatter)

            // Define the desired output format pattern
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

            // Format the LocalDateTime using the output formatter
            val formattedDateTime = localDateTime.format(outputFormatter)

            return formattedDateTime
        }
        else{
            return ""
        }
    } catch (e: Exception) {
        return e.toString()
    }
}