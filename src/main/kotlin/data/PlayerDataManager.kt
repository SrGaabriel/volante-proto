package data

import html.DataFetcher
import org.jetbrains.kotlinx.dataframe.DataFrame

class PlayerDataManager(

) {
    private var playersDataframe: DataFrame<*> = DataFrame.Empty

    suspend fun loadPage(
        season: String,
        competition: String,
        dataset: String
    ) {
        val url = "https://fbref.com/pt/comps/$season/$dataset/$competition-Estatisticas"
        val page = DataFetcher.fetchData(url)

    }
}