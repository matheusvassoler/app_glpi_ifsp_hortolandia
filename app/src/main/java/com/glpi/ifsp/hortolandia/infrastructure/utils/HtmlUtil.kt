package com.glpi.ifsp.hortolandia.infrastructure.utils

import android.os.Build
import android.text.Html

fun String.removeUnicodeHtmlTag(): String {
    return this.replace("&lt;p&gt;", "")
        .replace("&lt;/p&gt;", "")
        .replace("&lt;ul&gt;", "")
        .replace("&lt;li&gt;", "")
        .replace("&lt;/li&gt;", "")
        .replace("&lt;/ul&gt;", "")
        .replace("-&gt;", ".")
        .replace("  ", "")
        .replace(".", ". ")
        .replace(";", ". ")
        .replace(" .", ".")
        .replace(".  ", ". ")
}

fun String.removeHtmlH3Tag(): String {
    return this.replace("&lt;h3&gt", "")
        .replace("&lt;/h3&gt;", "")
        .replace(";", "")
}

fun String.convertUnicodeHtmlToText(): String {
    val unicodeToHtmlTag = htmlDecode(this)
    return htmlDecode(unicodeToHtmlTag)
}

private fun htmlDecode(text: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(text).toString()
    }
}
