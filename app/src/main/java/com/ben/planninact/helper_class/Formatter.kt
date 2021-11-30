package com.ben.planninact.helper_class

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import android.R
import java.lang.StringBuilder


class Formatter {

    fun stripHtml(html: String): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

    fun stripHtml1(html1: String): String {

        val html = html1.replace("<br />", "")
        val html2 = html.replace("</p>", "")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html2, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html2).toString()
        }
    }


    fun getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open("plupa_response.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }




}