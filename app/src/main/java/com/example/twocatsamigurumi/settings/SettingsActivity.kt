package com.example.twocatsamigurumi.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.twocatsamigurumi.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.menu_title_settings)
        }
        val fragment = SettingsFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.containerMain, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}