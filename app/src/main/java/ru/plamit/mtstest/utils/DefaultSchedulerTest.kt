package ru.plamit.mtstest.utils

import io.reactivex.*
import io.reactivex.schedulers.Schedulers

/**
 * Scheduler implementation for tests
 */
class DefaultSchedulerTest: IDefaultScheduler {
    override fun <T> apply() = ObservableTransformer<T, T> { upstream: Observable<T> ->
        upstream.subscribeOn(Schedulers.single()).observeOn(Schedulers.single())
    }

    override fun applyComp() = CompletableTransformer { upstream: Completable ->
        upstream.subscribeOn(Schedulers.single()).observeOn(Schedulers.single())
    }

    override fun <T> applySingle() = SingleTransformer { upstream: Single<T> ->
        upstream.subscribeOn(Schedulers.single()).observeOn(Schedulers.single())
    }
}