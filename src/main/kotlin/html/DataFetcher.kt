package html

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object DataFetcher {
    suspend fun fetchData(url: String): String {
        val client = HttpClient(CIO)
        val body = client.get(url) {
            userAgent("Mozilla 5.0")
        }.bodyAsText()
        return body
    }
}