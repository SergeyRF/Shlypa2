package com.example.sergey.shlypa2.screens.splash

import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.db.Contract
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.utils.DbExporter
import com.example.sergey.shlypa2.utils.PreferencesProvider
import com.example.sergey.shlypa2.utils.TriggerLiveData
import com.example.sergey.shlypa2.utils.coroutines.CoroutineViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LaunchViewModel(private val dispatchers: DispatchersProvider,
                      private val preferencesProvider: PreferencesProvider,
                      private val dataProvider: DataProvider,
                      private val dbExporter: DbExporter,
                      private val adsManager: AdsManager) : CoroutineViewModel(dispatchers.uiDispatcher) {

    companion object {
        private const val DB_IMPORTED = "db_imported_v1_2"
        private const val STATES_MIGRATIONS_DONE = "states_migrations_done"
    }


    val startMainLD = TriggerLiveData()

    init {
        initData()
    }

    private fun initData() = launch {
        withContext(dispatchers.ioDispatcher) {
            checkDb()
            adsManager.initAds()
        }

        startMainLD.call()
    }

    private fun checkDb() {
        when {
            preferencesProvider[DB_IMPORTED, false] != true -> {
                val success = dbExporter.importDbFromAsset(Contract.DB_NAME, Contract.DB_FILE_NAME)
                preferencesProvider[DB_IMPORTED] = success
                preferencesProvider[STATES_MIGRATIONS_DONE] = true
            }
            preferencesProvider[STATES_MIGRATIONS_DONE, false] != true -> {
                dataProvider.copyLegacyStates()
                preferencesProvider[STATES_MIGRATIONS_DONE] = true
            }
            else -> {
                Timber.d("TESTING Db already imported")
            }
        }
    }
}