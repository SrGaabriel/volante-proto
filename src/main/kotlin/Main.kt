import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.*
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tr
import java.io.File

suspend fun main() {
    val data = fetchScrapedData()
    parseScrapedData(data)
}

suspend fun fetchData(): String {
    val client = HttpClient(CIO)
    val body = client.get("https://fbref.com/pt/comps/9/stats/Premier-League-Estatisticas") {
        userAgent("Mozilla 5.0")
    }.bodyAsText()
    return body
}

suspend fun fetchScrapedData(): String {
    val body = skrape(AsyncFetcher) {
        request {
            url = "https://fbref.com/pt/comps/9/stats/Premier-League-Estatisticas"
            method = Method.GET
            userAgent = "Mozilla 5.0"
        }
        response { responseBody }
    }
    return body
}

suspend fun parseScrapedData(data: String) {
    return htmlDocument(data) {
        val rows = table(".stats_table") {
            findSecond {
                tr {
                    findAll{this}
                }
            }
        }
        print(rows)
    }
}