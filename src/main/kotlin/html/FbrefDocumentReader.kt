package html

import it.skrape.core.htmlDocument
import it.skrape.selects.CssSelectable
import it.skrape.selects.DocElement
import it.skrape.selects.html5.table
import it.skrape.selects.html5.td
import it.skrape.selects.html5.th
import it.skrape.selects.html5.tr
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
                    if (until != null && index >= until-1) return@mapIndexedNotNull null
                    if (index < from) return@mapIndexedNotNull null
                    FbrefDocumentReader(element).scope()
                }
            }
        }
    }

    fun <T> mapCells(scope: (DocElement) -> T): List<T> {
        return element.td {
            findAll {
                map {
                    scope(it)
                }
            }
        }
    }

    fun readOverheader(index: Int = 0): Map<String, Int> {
        return mapRow(index) {
            element.th {
                findAll {
                    associate { element ->
                        val span = element.dataAttributes["colspan"]?.toIntOrNull() ?: 1
                        element.text to span
                    }
                }
            }
        }
    }

    fun <T> readAndJoinHeaders(size: Int = 2): List<String> {
        val overheader = readOverheader()
        mapRows(from=1, until=size) {
            element.th {
                findAll {
                    mapIndexed { index, docElement ->
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
fun <T> readFbrefDocument(page: String, scope: FbrefDocumentReader.() -> T): T {
    contract {
        callsInPlace(scope, InvocationKind.EXACTLY_ONCE)
    }
    return FbrefDocumentReader.readDocument(page).scope()
}