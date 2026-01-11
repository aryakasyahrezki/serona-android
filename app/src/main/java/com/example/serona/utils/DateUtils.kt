package com.example.serona.utils

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils{

    fun formatBirthDate(
        day: String,
        month: String,
        year: String
    ): String = runCatching {

        val dayInt = day.toInt()
        val yearInt = year.toInt()

        val monthInt = when (month.lowercase()) {
            "jan" -> 1
            "feb" -> 2
            "mar" -> 3
            "apr" -> 4
            "may" -> 5
            "jun" -> 6
            "jul" -> 7
            "aug" -> 8
            "sep" -> 9
            "oct" -> 10
            "nov" -> 11
            "dec" -> 12
            else -> throw IllegalArgumentException("Invalid month")
        }

        String.format(Locale.US, "%04d-%02d-%02d", yearInt, monthInt, dayInt)

    }.getOrDefault("-")

}