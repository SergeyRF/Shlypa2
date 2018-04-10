package com.example.sergey.shlypa2

import android.app.Instrumentation
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game.getPlayers
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

/**
 * Created by alex on 4/10/18.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DbTests {

    lateinit var dataBase: DataBase

    @Test
    fun testPlayersInserting() {
        dataBase = DataBase.getInstance(RuntimeEnvironment.application)
        val player = Player(name = "Ashot")
        val firstId = dataBase.playersDao().insertPlayer(player)

        val playerFromDb = dataBase.playersDao().getPlayerById(firstId)

        assertEquals(player.name, playerFromDb?.name)

        val listOfPlayers = dataBase.playersDao().getAllPlayers()
        for(pl in listOfPlayers) {
            println(pl)
        }

        assertEquals(1, listOfPlayers.size)

        dataBase.playersDao().insertPlayer(player)
        val new = dataBase.playersDao().getAllPlayers()
        assertEquals(1,new.size)

        dataBase.playersDao().insertPlayer(Player("Vazgen"))
        val thirdList = dataBase.playersDao().getAllPlayers()
        for(pl in thirdList) {
            println(pl)
        }
        assertEquals(2, thirdList.size)
    }

    @After
    fun shutdown() {
        dataBase.close()
    }
}