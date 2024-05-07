package util

class Overheader(
    private val headerSpans: Map<String, Int>
) {
    fun getOverheaderByIndex(index: Int): String {
        require(index >= 0) { "Index must be greater than or equal to 0" }
        require(index < headerSpans.values.sum()) { "Index must be less than the sum of all header spans" }
        var count = 0
        for ((key, value) in headerSpans) {
            count += value
            if (count > index) {
                return key
            }
        }
        error("Impossible to reach")
    }

    companion object {
        fun fromMap(headerSpans: Map<String, Int>): Overheader =
            Overheader(headerSpans)
    }
}