package ru.plamit.mtstest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.koin.android.ext.android.inject
import ru.plamit.mtstest.backend.storage.Storage
import ru.plamit.mtstest.usersList.ui.UsersListFragment

class MainActivity : AppCompatActivity() {

    private val storage: Storage by inject()

    companion object {
        private const val USERS_LIST = "USERS_LIST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, UsersListFragment.newInstance(), USERS_LIST)
                    .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.usePicasso-> storage.libraryUse = "picasso"
            R.id.useGlide-> storage.libraryUse = "glide"
            else->{}
        }
        return true
    }

    override fun onBackPressed() {
        val list = supportFragmentManager.findFragmentByTag(USERS_LIST)
        if (list == null) {
            super.onBackPressed()
            return
        }
        if ((list as UsersListFragment).onBackPressed()) super.onBackPressed()
    }
}
