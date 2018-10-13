package ru.plamit.mtstest.di

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.plamit.mtstest.backend.api.RetrofitBuilder
import ru.plamit.mtstest.usersList.IUsersListInteractor
import ru.plamit.mtstest.usersList.UsersListInteractor
import ru.plamit.mtstest.usersList.ui.UsersListViewModel
import ru.plamit.mtstest.utils.DefaultScheduler
import ru.plamit.mtstest.utils.IDefaultScheduler

val usersListModule = module {
    single { RetrofitBuilder().createApi() }
    single { DefaultScheduler() as IDefaultScheduler }
    single { UsersListInteractor(get(), get()) as IUsersListInteractor }
    viewModel { UsersListViewModel(get()) }
}