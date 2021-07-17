package com.glpi.ifsp.hortolandia.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TicketUI(
    val id: String,
    val title: String,
    val description: String,
    val openingDate: String,
    val openingHour: String,
    val updateDate: String,
    val updateHour: String,
    val status: String,
    val percentageStatusProgress: Int
) : Parcelable
