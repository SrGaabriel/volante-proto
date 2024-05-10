package parser

import html.readFbrefDocument
import it.skrape.selects.html5.a
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf

object PlayerTableParser {
    fun parse(page: String, index: Int = 2): DataFrame<*> = readFbrefDocument(page) {
        selectStatsTable(index) {
            val columns = listOf("Id") + readAndJoinHeaders()
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
                listOf(id ?: error("Id not found for $indicator")) + indicator + cells
            }

            dataFrameOf(
                columns,
                values
            )
        }
    }
}