package ru.plamit.mtstest.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultScheduler: IDefaultScheduler {
    override fun <T> apply() = ObservableTransformer<T, T> { upstream: Observable<T> ->
        upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun applyComp() = CompletableTransformer { upstream: Completable ->
        upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun <T> applySingle() = SingleTransformer { upstream: Single<T> ->
        upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}