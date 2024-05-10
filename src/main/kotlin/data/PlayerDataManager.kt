package data

import html.DataFetcher
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.concat
import org.jetbrains.kotlinx.dataframe.api.join
import parser.PlayerTableParser
import struct.Competition

class PlayerDataManager {

    suspend fun loadCompetitions(
        vararg competition: Competition
    ): DataFrame<*> {
        var final: DataFrame<*>? = null
        competition.forEach {
            val outfield = loadCompetition(it)
            final = if (final == null) {
                outfield
            } else {
                final!!.concat(outfield)
            }
        }
        return final ?: error("No data found for competitions")
    }

    suspend fun loadCompetition(competition: Competition): DataFrame<*> {
        var final: DataFrame<*>? = null
        for (dataset in OUTFIELD_DATASETS) {
            final = final?.join(parsePage(competition, dataset)) ?: parsePage(competition, dataset)
        }
        return final ?: error("No data found for $competition")
    }

    suspend fun parsePage(
        competition: Competition,
        dataset: String
    ): DataFrame<*> {
        val url = "https://fbref.com/pt/comps${competition.getPath(dataset)}"
        val page = DataFetcher.fetchData(url)
        return competition.refactorDataFrame(
            dataFrame = PlayerTableParser.parse(page, competition.customIndex ?: 2)
        )
    }

    companion object {
        val OUTFIELD_DATASETS = arrayOf(
            "stats",
            "shooting"
        )
    }
}