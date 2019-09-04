package com.example.sergey.shlypa2.di

import android.content.Context
import androidx.room.Room
import com.example.sergey.shlypa2.BuildConfig
import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.ads.ConsentManager
import com.example.sergey.shlypa2.data.AppLifecycleObserver
import com.example.sergey.shlypa2.data.ConfigsProvider
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.Contract
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.screens.game.RoundViewModel
import com.example.sergey.shlypa2.screens.game_result.GameResultViewModel
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsViewModel
import com.example.sergey.shlypa2.screens.main.WelcomeViewModel
import com.example.sergey.shlypa2.screens.players.PlayersViewModel
import com.example.sergey.shlypa2.screens.players.dialog.PlayerSelectViewModel
import com.example.sergey.shlypa2.screens.settings.SettingsViewModel
import com.example.sergey.shlypa2.screens.splash.LaunchViewModel
import com.example.sergey.shlypa2.screens.words_in.WordsViewModel
import com.example.sergey.shlypa2.utils.DbExporter
import com.example.sergey.shlypa2.utils.PreferencesProvider
import com.example.sergey.shlypa2.utils.SoundManager
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.anal.FlurryFacadeDebug
import com.example.sergey.shlypa2.utils.anal.FlurryFacadeRelease
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProviderImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel(get(), get(), get()) }
    viewModel { PlayersViewModel(get(), get(), get(), get(), get()) }
    viewModel { GameSettingsViewModel(get(), get(), get(), get()) }
    viewModel { RoundViewModel(get(), get(), get(), get(), get()) }
    viewModel { WordsViewModel(get(), get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { LaunchViewModel(get(), get(), get(), get()) }
    viewModel { PlayerSelectViewModel(get(), get()) }
    viewModel { GameResultViewModel(get(), get()) }

    single<DispatchersProvider> { DispatchersProviderImpl() }

    single { createGson() }

    single { createDb(get()) }
    single { (db: DataBase) -> db.typesDap() }
    single { (db: DataBase) -> db.playersDao() }
    single { (db: DataBase) -> db.stateDao() }
    single { (db: DataBase) -> db.wordDao() }

    single { DataProvider(get(), get(), get()) }

    single { FirebaseAnalytics.getInstance(get()) }
    single { if (BuildConfig.DEBUG) FlurryFacadeDebug() else FlurryFacadeRelease() }
    single { AnalSender(get(), get()) }
    single { PlayersRepository(get()) }
    single { PreferencesProvider(get(), null) }
    single { SoundManager(get(), get()) }
    single { ConsentManager(get()) }
    single { AdsManager(get(), get()) }
    single { FirebaseRemoteConfig.getInstance() }
    single { ConfigsProvider(get()) }
    single { AppLifecycleObserver(get())}
    factory { DbExporter(get()) }
}

private fun createDb(context: Context): DataBase {
    return Room.databaseBuilder(context, DataBase::class.java, Contract.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
}

private fun createGson() = GsonBuilder()
        .setPrettyPrinting()
        .create()


