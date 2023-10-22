package com.example.twocatsamigurumi.fcm

import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.core.content.edit
import com.example.twocatsamigurumi.Constants
import com.google.firebase.messaging.FirebaseMessagingService

class FCMService: FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        registerNewTokenLocal (newToken)
    }

    private fun registerNewTokenLocal (newToken: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.edit {
            putString(Constants.PROP_TOKEN, newToken)
                .apply()
        }
        Log.i("abc", newToken)
    }

}