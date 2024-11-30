package com.netanel.xplore.auth.repository.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */

@Serializable
data class User(
    val name: String,
    @SerializedName(value = "phone_number")
    val phoneNumber: String
)