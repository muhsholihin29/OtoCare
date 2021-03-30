package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TimeSlot(
    var id: Int = 0,
    var timeSlot: String = "",
    var available: Boolean = true
) : Parcelable