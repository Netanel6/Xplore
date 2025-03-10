package com.netanel.xplore.utils


/**
 * Created by netanelamar on 01/03/2025.
 * NetanelCA2@gmail.com
 */

fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> "$hours שעות ${if (minutes > 0) "$minutes דקות" else ""} ${if (seconds > 0) "$seconds שניות" else ""}"
        minutes > 0 -> "$minutes דקות ${if (seconds > 0) "$seconds שניות" else ""}"
        else -> "$seconds שניות"
    }.trim()
}