package com.example.serona.data.source

import android.content.Context
import com.example.serona.R
import com.example.serona.data.model.PrivacyFaq

class PrivacyDataProvider(private val context: Context) {
    fun getFaqs(): List<PrivacyFaq> {
        return listOf(
            PrivacyFaq(
                question = context.getString(R.string.faq_q1),
                answer = context.getString(R.string.faq_a1).trimIndent()
            ),

            PrivacyFaq(
                question = context.getString(R.string.faq_q2),
                answer = context.getString(R.string.faq_a2).trimIndent()
            ),

            PrivacyFaq(
                question = context.getString(R.string.faq_q3),
                answer = context.getString(R.string.faq_a3).trimIndent()
            ),

            PrivacyFaq(
                question = context.getString(R.string.faq_q4),
                answer = context.getString(R.string.faq_a4).trimIndent()
            ),

            PrivacyFaq(
                question = context.getString(R.string.faq_q5),
                answer = context.getString(R.string.faq_a5).trimIndent()
            ),

            PrivacyFaq(
                question = context.getString(R.string.faq_q6),
                answer = context.getString(R.string.faq_a6).trimIndent()
            )
        )
    }
}