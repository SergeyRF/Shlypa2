package com.example.sergey.shlypa2.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import timber.log.Timber
import java.io.*
import java.nio.channels.FileChannel

/**
 * Created by alex on 5/2/18.
 */
class DbExporter {

    fun exportDbToFile(context: Context, dbName: String) {
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()
        Timber.d("External storage is $sd")
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath = context.getDatabasePath(dbName).toString()
        val backupDBPath = dbName
        val currentDB = File(currentDBPath)
        val backupDB = File(sd, backupDBPath)
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination!!.transferFrom(source, 0, source!!.size())
            source.close()
            destination.close()
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun importDbFromAsset(context: Context, dbName: String): Boolean {
        Timber.d("Import db from assets")
        var dbIs: InputStream? = null
        var dbOus: FileOutputStream? = null
        val dbFileTarget = context.getDatabasePath(dbName)
        val data = Environment.getDataDirectory()
        try {
            dbFileTarget.parentFile.mkdirs()
            dbOus = FileOutputStream(dbFileTarget)
            dbIs = context.assets.open(dbName)

            val buffer = ByteArray(1024)
            var read = dbIs!!.read(buffer)

            while (read != -1) {
                dbOus.write(buffer)
                read = dbIs.read(buffer)
            }

            dbIs.close()
            dbOus.flush()
            dbOus.close()

            Timber.d("Db imported from assets")

        } catch (ex: IOException) {
            Timber.e(ex)
        }

        return dbFileTarget.exists()
    }
}