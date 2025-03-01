package com.netanel.xplore.utils


/**
 * Created by netanelamar on 01/03/2025.
 * NetanelCA2@gmail.com
 */

fun Int.formatTime(): String {
    return when {
        this >= 3600 -> "${this / 3600} שעות"
        this >= 60 -> "${this / 60} דקות"
        else -> "$this שניות"
    }
}
