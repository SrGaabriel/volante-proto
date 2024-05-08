package data

import html.DataFetcher
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.join
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import parser.PlayerTableParser
import struct.Competition
import java.io.File

class PlayerDataManager(

) {
    private var playersDataframe: DataFrame<*> = DataFrame.Empty

    suspend fun loadPage(
        competition: Competition,
        dataset: String
    ) {
        val url = "https://fbref.com/pt/comps/${competition.id}/$dataset/${competition.name}-Estatisticas"
        val page = DataFetcher.fetchData(url)
        playersDataframe = playersDataframe.join(PlayerTableParser.parse(page))
    }

    suspend fun toExcel() {
        playersDataframe.writeExcel("data.xlsx")
    }
}