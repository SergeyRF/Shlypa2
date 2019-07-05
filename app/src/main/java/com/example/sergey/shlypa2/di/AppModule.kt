package com.example.sergey.shlypa2.di

import android.content.Context
import androidx.room.Room
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.Contract
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsViewModel
import com.example.sergey.shlypa2.screens.players.PlayersViewModel
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProviderImpl
import com.example.sergey.shlypa2.screens.game.RoundViewModel
import com.example.sergey.shlypa2.screens.main.WelcomeViewModel
import com.example.sergey.shlypa2.viewModel.WordsViewModel
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel(get(), get(), get()) }
    viewModel { PlayersViewModel(get(), get(), get(), get(), get()) }
    viewModel { GameSettingsViewModel(get(), get(), get()) }
    viewModel { RoundViewModel(get(), get(), get()) }
    viewModel { WordsViewModel(get(), get(), get()) }

    single<DispatchersProvider> { DispatchersProviderImpl() }

    single { createGson() }

    single { createDb(get()) }
    single { (db: DataBase) -> db.typesDap() }
    single { (db: DataBase) -> db.playersDao() }
    single { (db: DataBase) -> db.stateDao() }
    single { (db: DataBase) -> db.wordDao() }

    single { DataProvider(get(), get(), get()) }

    single { AnalSender(get())}
    single { PlayersRepository(get())}
}

private fun createDb(context: Context): DataBase {
    return Room.databaseBuilder(context, DataBase::class.java, Contract.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
}

private fun createGson() = GsonBuilder()
        .setPrettyPrinting()
        .create()


