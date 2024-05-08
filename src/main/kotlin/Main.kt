import data.PlayerDataManager
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import parser.PlayerTableParser
import struct.Competition
import struct.Competitions
import java.io.File

suspend fun main() {
//    val playerData = fetchData("https://fbref.com/pt/comps/24/stats/Serie-A-Estatisticas")
    val dataManager = PlayerDataManager()
    dataManager.loadPage(
        Competitions.Brasileirao,
        dataset = "shooting"
    )
    dataManager.loadPage(
        Competitions.PremierLeague,
        dataset = "stats"
    )
    dataManager.toExcel()
}

