package com.example.sergey.shlypa2.screens.splash

import com.example.sergey.shlypa2.db.Contract
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
                      private val dbExporter: DbExporter) : CoroutineViewModel(dispatchers.uiDispatcher) {

    companion object {
        private const val DB_IMPORTED = "db_imported_v1_2"
    }


    val startMainLD = TriggerLiveData()

    init {
        checkDb()
    }

    private fun checkDb() = launch {
        if (preferencesProvider[DB_IMPORTED, false] != true) {
            val success = withContext(dispatchers.ioDispatcher) {
                dbExporter.importDbFromAsset(Contract.DB_NAME, Contract.DB_FILE_NAME)
            }
            preferencesProvider[DB_IMPORTED] = success
        } else {
            Timber.d("TESTING Db already imported")
        }

        startMainLD.call()
    }
}