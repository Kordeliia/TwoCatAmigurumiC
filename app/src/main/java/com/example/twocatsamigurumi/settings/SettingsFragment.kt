package com.example.twocatsamigurumi.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.twocatsamigurumi.R
import com.google.firebase.ktx.Firebase


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val switchPreferencesCompat = findPreference<SwitchPreferenceCompat>(getString(R.string.pref_offers_key))
        switchPreferencesCompat?.setOnPreferenceChangeListener { preference, newValue ->
            (newValue as? Boolean)?.let {isChecked->
                val topic = getString(R.string.settings_topic_offers)
                if(isChecked){
                    Toast.makeText(context,
                        getString(R.string.toast_notificaciones_active),
                        Toast.LENGTH_SHORT).show()
                    /*Firebase.messaging.suscribeToTopic(topic)
                        .addOnSuccessListener{
                            Toast.makeText(context,
                                getString(R.string.toast_notificaciones_active),
                                Toast.LENGTH_SHORT).show()
                        }*/
                } else {

                    Toast.makeText(context,
                        getString(R.string.toast_notificaciones_not_active),
                        Toast.LENGTH_SHORT).show()
                }
                  /*  Firebase.messaging.unsubscribeToTopic(topic)
                        .addOnSuccessListener{
                            Toast.makeText(context,
                                getString(R.string.toast_notificaciones_not_active),
                                Toast.LENGTH_SHORT).show()
                        }
                }*/
            }
            true
        }
    }
}