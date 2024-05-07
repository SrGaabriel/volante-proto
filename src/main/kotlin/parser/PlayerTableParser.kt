package parser

import html.readFbrefDocument
import it.skrape.selects.html5.a
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf

object PlayerTableParser: TableParser {
    override fun parse(page: String): DataFrame<*> = readFbrefDocument(page) {
        selectStatsTable(index = 2) {
            val columns = readAndJoinHeaders(2) + "Id"
            val values = flatMapRows(from = 2) {
                var id: String? = null
                val indicator = readIndicator()
                val cells = mapCells {
                    val dataStat = it.dataAttributes["data-stat"]
                    if (dataStat == "player") {
                        id = it.a {
                            findFirst {
                                attribute("href")
                                    .split('/')[3]
                            }
                        }
                    }
                    it.text
                }
                listOf(indicator) + cells + (id ?: error("Error"))
            }

            dataFrameOf(
                columns,
                values
            )
        }
    }
}