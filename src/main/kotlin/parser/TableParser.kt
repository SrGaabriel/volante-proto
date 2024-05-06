package parser

import org.jetbrains.kotlinx.dataframe.api.DataFrameBuilder

interface TableParser {
    fun parse(page: String): DataFrameBuilder
}