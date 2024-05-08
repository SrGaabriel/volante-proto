package struct

data class Competition(
    val id: Int,
    val name: String
)

object Competitions {
    val Brasileirao = Competition(24, "Serie-A")

    val PremierLeague = Competition(9, "Premier-League")
}