package com.example.sergey.shlypa2.di

import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.screens.players.PlayersViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProviderImpl
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { PlayersViewModel(get(), get()) }
    viewModel { GameSettingsViewModel(get(), get(), get()) }
    single<DispatchersProvider> { DispatchersProviderImpl() }
    single<DataProvider> { DataProvider(get())}
}

