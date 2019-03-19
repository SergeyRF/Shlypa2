package com.example.sergey.shlypa2.di

import com.example.sergey.shlypa2.screens.players.PlayersViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { PlayersViewModel(get()) }
}