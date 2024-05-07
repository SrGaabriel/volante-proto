package html

import it.skrape.core.htmlDocument
import it.skrape.selects.CssSelectable
import it.skrape.selects.DocElement
import it.skrape.selects.html5.table
import it.skrape.selects.html5.td
import it.skrape.selects.html5.th
import it.skrape.selects.html5.tr
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.DataFrameBuilder
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import util.Overheader
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@JvmInline
value class FbrefDocumentReader(private val element: CssSelectable) {
    fun <T> selectTable(cssSelector: String, index: Int, scope: FbrefDocumentReader.() -> T): T {
        return element.table(cssSelector) {
            findByIndex(index) {
                FbrefDocumentReader(this).scope()
            }
        }
    }

    fun <T> selectStatsTable(index: Int, scope: FbrefDocumentReader.() -> T): T =
        selectTable(".stats_table", index, scope)

    fun <T : Any> mapRow(index: Int, scope: FbrefDocumentReader.() -> T): T =
        element.tr {
            findByIndex(index) {
                FbrefDocumentReader(this).scope()
            }
        }

    fun <T : Any> mapRows(until: Int? = null, from: Int = 0, scope: FbrefDocumentReader.() -> T): List<T> {
        return element.tr {
            findAll {
                mapIndexedNotNull { index, element ->
                    if (element.hasClass("thead")) return@mapIndexedNotNull null
                    if (until != null && index >= until-1) return@mapIndexedNotNull null
                    if (index < from) return@mapIndexedNotNull null
                    FbrefDocumentReader(element).scope()
                }
            }
        }
    }

    fun <T : Any> flatMapRows(until: Int? = null, from: Int = 0, scope: FbrefDocumentReader.() -> List<T>): List<T>  =
        mapRows(until, from) { scope() }.flatten()

    fun <T> mapCells(scope: (DocElement) -> T): List<T> {
        return element.td {
            findAll {
                map {
                    scope(it)
                }
            }
        }
    }

    fun readOverheader(index: Int = 0): Overheader {
        return mapRow(index) {
            element.th {
                findAll {
                    Overheader.fromMap(buildMap {
                        for (element in this@findAll) {
                            val span = if (element.hasAttribute("colspan")) {
                                element.attribute("colspan").toInt()
                            } else {
                                1
                            }
                            val text = if (containsKey(element.text)) element.text + " n2".trimMargin()
                            else element.text

                            put(text, span)
                        }
                    })
                }
            }
        }
    }

    fun readAndJoinHeaders(size: Int = 2): List<String> {
        val overheader = if (size > 1) readOverheader() else null
        return flatMapRows(from=1, until=size+1) {
            element.th {
                findAll {
                    mapIndexed { index, docElement ->
                        val header = overheader?.getOverheaderByIndex(index)
                        if (header.isNullOrBlank())
                            docElement.text
                        else
                            "$header - ${docElement.text}"
                    }
                }
            }
        }
    }

    fun readIndicator(): String {
        return element.th {
            findFirst {
                text
            }
        }
    }

    companion object {
        fun readDocument(page: String): FbrefDocumentReader {
            return FbrefDocumentReader(
                htmlDocument(page)
            )
        }
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> readFbrefDocument(page: String, scope: FbrefDocumentReader.() -> T): T {
    contract {
        callsInPlace(scope, InvocationKind.EXACTLY_ONCE)
    }
    return FbrefDocumentReader.readDocument(page).scope()
}

fun readStandardFbrefDataframe(
    page: String,
    index: Int,
    headerSize: Int = 2
): DataFrame<*> = readFbrefDocument(page) {
    selectStatsTable(index = index) {
        val columns = readAndJoinHeaders(headerSize)
        val values = flatMapRows(from = 2) {
            val indicator = readIndicator()
            val cells = mapCells {
                it.text
            }
            listOf(indicator) + cells
        }

        dataFrameOf(
            columns,
            values
        )
    }
}