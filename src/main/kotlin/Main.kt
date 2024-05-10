import data.PlayerDataManager
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jetbrains.kotlinx.dataframe.api.concat
import org.jetbrains.kotlinx.dataframe.api.copy
import org.jetbrains.kotlinx.dataframe.api.join
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import parser.PlayerTableParser
import struct.Competition
import struct.Competitions
import java.io.File

suspend fun main() {
    val dataManager = PlayerDataManager()
    val result = dataManager.loadCompetitions(
        Competitions.Brasileirao,
        Competitions.PremierLeague
    )
    result.writeExcel("result.xlsx")
}

