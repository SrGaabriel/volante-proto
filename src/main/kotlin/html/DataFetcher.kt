package html

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import struct.Competition
import java.io.File

object DataFetcher {
    suspend fun fetchData(url: String): String {
        println(url)
        val cache = loadCache(url)
        if (cache != null) return cache
        val client = HttpClient(CIO)
        val body = client.get(url) {
            userAgent("Mozilla 5.0")
        }.bodyAsText()
        val fixed = fixData(body)
        saveCache(url, fixed)
        return fixed
    }

    fun fixData(data: String): String {
        val regex = Regex("<!--(.*?)-->", setOf(RegexOption.DOT_MATCHES_ALL))
        val matches = regex.findAll(data)
        var fixedData = data
        for (match in matches) {
            if (!match.value.contains("<table")) {
                fixedData = fixedData.replace(match.value, "")
            } else {
                fixedData = fixedData.replace(match.value, match.value.replace("<!--", "").replace("-->", ""))
            }
        }
        return fixedData
    }

    fun loadCache(url: String): String? {
        val cacheDir = getCacheDir()
        val file = cacheDir.resolve(url.hashCode().toString() + ".html")
        if (!file.exists()) return null
        return file.readText()
    }

    fun saveCache(url: String, data: String) {
        val cacheDir = getCacheDir()
        val file = cacheDir.resolve(url.hashCode().toString() + ".html")
        file.writeText(data)
        file.createNewFile()
    }

    fun getHomeDir(): File {
        val userHome = System.getProperty("user.home")
        val homeDir = File("$userHome/volante")
        homeDir.mkdirs()
        return homeDir
    }

    fun getCacheDir(): File {
        val homeDir = getHomeDir()
        val date = java.time.LocalDate.now()
        val cacheDir = homeDir.resolve("cache").resolve(date.toString())
        cacheDir.mkdirs()
        return cacheDir
    }
}