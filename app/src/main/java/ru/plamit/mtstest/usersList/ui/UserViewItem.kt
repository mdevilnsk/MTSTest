package ru.plamit.mtstest.usersList.ui

data class UserViewItem(
        val avatar: String,
        val login: String,

        val name: String? = null,
        val companyName: String? = null,
        val email: String? = null
)