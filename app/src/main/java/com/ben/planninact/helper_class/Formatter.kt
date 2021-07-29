package com.ben.planninact.helper_class

import android.os.Build
import android.text.Html

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

}