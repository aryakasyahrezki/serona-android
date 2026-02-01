package com.example.serona.utils

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils{

    private fun getMonthInt(month: String): Int {
        return when (month.lowercase()) {
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
    }

    fun isValidDate(day: String, month: String, year: String): Boolean {
        return runCatching {
            val d = day.toInt()
            val y = year.toInt()
            val m = getMonthInt(month)

            if (m == -1) return false

            // LocalDate akan melempar exception jika tanggal tidak logis (ex: 29 Feb 2023)
            java.time.LocalDate.of(y, m, d)
            true
        }.getOrDefault(false)
    }

    fun formatBirthDate(
        day: String,
        month: String,
        year: String
    ): String = runCatching {

        val dayInt = day.toInt()
        val yearInt = year.toInt()

        val monthInt = getMonthInt(month)

        String.format(Locale.US, "%04d-%02d-%02d", yearInt, monthInt, dayInt)

    }.getOrDefault("-")

    fun parseBirthDate(dateStr: String?): Triple<String, String, String>? {
        if (dateStr.isNullOrBlank() || dateStr == "-") return null

        return runCatching {
            val parts = dateStr.trim().split(" ")
            if (parts.size != 3) return null

            val day = parts[0].toInt().toString()

            val fullMonth = parts[1]
            val monthShort = if (fullMonth.length >= 3) {
                fullMonth.substring(0, 3).lowercase().replaceFirstChar { it.uppercase() }
            } else "Jan"

            val year = parts[2]

            Triple(day, monthShort, year)
        }.getOrNull()
    }

}