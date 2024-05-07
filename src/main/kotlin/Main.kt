import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import parser.PlayerTableParser
import java.io.File

suspend fun main() {
//    val playerData = fetchData("https://fbref.com/pt/comps/24/stats/Serie-A-Estatisticas")
    val playerData = File("C:\\Users\\gaabr\\IdeaProjects\\volante\\src\\main\\resources\\assets\\page.html").readText()
    parseScrapedData(playerData)
}

suspend fun fetchData(url: String): String {
    val client = HttpClient(CIO)
    val body = client.get(url) {
        userAgent("Mozilla 5.0")
    }.bodyAsText()
    return body
}

suspend fun parseScrapedData(data: String) {
    val dataframe = PlayerTableParser.parse(data)
    dataframe.writeExcel(File("C:\\Users\\gaabr\\IdeaProjects\\volante\\src\\main\\resources\\assets\\data.xlsx"))
}

