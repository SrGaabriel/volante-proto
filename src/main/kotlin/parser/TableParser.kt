package parser

import org.jetbrains.kotlinx.dataframe.DataFrame

interface TableParser {
    fun parse(page: String): DataFrame<*>
}