package ru.plamit.mtstest.utils

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

interface IDefaultScheduler {
    fun <T> apply(): ObservableTransformer<T, T>
    fun applyComp(): CompletableTransformer
    fun <T> applySingle(): SingleTransformer<T, T>
}