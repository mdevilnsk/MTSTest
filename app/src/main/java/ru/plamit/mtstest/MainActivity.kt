package ru.plamit.mtstest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import ru.plamit.mtstest.ui.usersList.UsersListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, UsersListFragment.newInstance())
                    .commitNow()
        }
    }
}
