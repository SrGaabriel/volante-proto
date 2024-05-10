package struct

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.remove

abstract class Competition(
    val id: Any,
    val name: String,
    val customIndex: Int? = null
) {
    open fun getPath(dataset: String): String {
        return "/$id/$dataset/$name-Estatisticas"
    }

    open fun refactorDataFrame(dataFrame: DataFrame<*>): DataFrame<*> {
        return dataFrame
    }
}

object Competitions {
    val Brasileirao = object : Competition(24, "Serie-A") {}

    val Top5EuropeanLeagues = object : Competition("Big5", "Maiores-5-Ligas-Europeias", customIndex = 1) {
        override fun getPath(dataset: String): String {
            return "/$id/$dataset/jogadores/$name-Estatisticas"
        }

        override fun refactorDataFrame(dataFrame: DataFrame<*>): DataFrame<*> {
            return dataFrame.remove("Camp.")
        }
    }

    val PremierLeague = object : Competition(9, "Premier-League") {}
}