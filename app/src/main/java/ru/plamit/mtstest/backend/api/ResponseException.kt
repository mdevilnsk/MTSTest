package ru.plamit.mtstest.backend.api

class ResponseException(val code: Int, message: String) : RuntimeException(message)