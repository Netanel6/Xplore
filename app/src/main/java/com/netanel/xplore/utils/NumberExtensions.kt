package com.netanel.xplore.utils


/**
 * Created by netanelamar on 01/03/2025.
 * NetanelCA2@gmail.com
 */

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return buildString {
        if (hours > 0) append("$hours שעות ")
        if (minutes > 0) append("$minutes דקות ")
        if (remainingSeconds > 0 || isEmpty()) append("$remainingSeconds שניות")
    }.trim()
}

