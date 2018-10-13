package ru.plamit.mtstest.backend.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.plamit.mtstest.entity.GithubUserInformation
import java.lang.reflect.Type


class Storage(ctx: Context) {

    private val gson = Gson()
    private var prefs: SharedPreferences
    var appId: String

    companion object {
        private const val PREFERENCE_NAME = "AppPreferences"
        private const val GIT_USERS_LIST = "GIT_USERS_LIST"
        private const val LIBRARY_USE = "LIBRARY_USE"
    }

    init {
        prefs = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        @SuppressLint("HardwareIds")
        appId = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun get(name: String): String = get(name, "")
    private fun <T> get(name: String, clazz: Class<T>) = gson.fromJson<T>(get(name), clazz)
    private fun <T> get(name: String, type: Type) = gson.fromJson<T>(get(name), type)

    @Suppress("UNCHECKED_CAST")
    private fun <T> get(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
        res as T
    }

    @SuppressLint("CommitPrefEdits")
    private fun <U> set(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, gson.toJson(value))
        }.apply()
    }

    var userInformation: List<GithubUserInformation>
        get() {
            val type = object : TypeToken<List<GithubUserInformation>>() {}.type
            val users = get<List<GithubUserInformation>>(GIT_USERS_LIST, type)
            return users ?: ArrayList()
        }
        set(value) {
            val shrinkedList: MutableList<GithubUserInformation> = ArrayList()
            var i = 0
            value.reversed().forEach loop@{
                if (i >= 100) return@loop
                i++
                shrinkedList.add(it)
            }

            set(GIT_USERS_LIST, shrinkedList)
        }

    var libraryUse: String
        get() = get(LIBRARY_USE, "picasso")
        set(value) = set(LIBRARY_USE, value)
}
