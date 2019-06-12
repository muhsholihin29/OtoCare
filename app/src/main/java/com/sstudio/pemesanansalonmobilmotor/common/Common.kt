package com.sstudio.pemesanansalonmobilmotor.common

import com.sstudio.pemesanansalonmobilmotor.Model.Bengkel
import com.sstudio.pemesanansalonmobilmotor.Model.Salon
import com.sstudio.pemesanansalonmobilmotor.Model.User
import java.util.*


object Common {
    var IS_LOGIN: String = "IsLogin"
    var currentUser: User? = null
    var KEY_ENABLE_BUTTON_NEXT: String = "ENABLE_BUTTON_NEXT"
    var KEY_SALON_STORE = "SALON_SAVE"
    var currentSalon: Salon? = null
    var step: Int = 0
    var city: String = ""
    val KEY_BENGKEL_LOAD_DONE: String = "BENGKEL_LOAD_DONE"
    var currentBengkel: Bengkel? = null
    val KEY_DISPLAY_TIME_SLOT: String = "DISPLAY_TIMA_SLOT"
    val KEY_STEP:String = "STEP"
    val KEY_BENGKEL_SELECTED: String = "BENGKEL_SELECTED"
    val TIME_SLOT_TOTAL: Int = 5
    fun convertTimeSlotToString(slot: Int): String{
        when(slot){
            0 -> return "7:00-9:00"
            1 -> return "9:00-11:00"
            2 -> return "11:00-13:00"
            3 -> return "13:00-15:00"
            4 -> return "16:00-17:00"
            else -> return "Tutup"
        }
    }
}