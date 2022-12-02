package Days

import java.io.File

fun day2()
{
    var sum = 0
    val fileURL = "./input.txt"
    File(fileURL).forEachLine { line: String ->
        val match = line.split(" ")
        val enemy = EnemyMapping[match[0]]!!
        val player = PlayerMapping[match[1]]!!
        val pointsPerMatch = calculatePointsPerMatch(enemy, player)
        val pointsPerPlay = calculatePointsPerPlay(player)
        sum += pointsPerMatch + pointsPerPlay
    }
    println("You will obtain $sum if everything goes as expected")
}

fun calculatePointsPerMatch(enemy: Mapping, me: Mapping): Int {
    return if (enemy.compareTo(me) == 0) 3 //DRAW
    else {
        when (enemy) {
            Mapping.ROCK -> if (me == Mapping.PAPER) 6 else 0

            Mapping.PAPER -> if (me == Mapping.SCISSORS) 6 else 0

            Mapping.SCISSORS -> if (me == Mapping.ROCK) 6 else 0
        }
    }
}

fun calculatePointsPerPlay(play: Mapping): Int {
    return when (play) {
        Mapping.ROCK -> 1
        Mapping.PAPER -> 2
        Mapping.SCISSORS -> 3
    }
}

val EnemyMapping: HashMap<String, Mapping> =
    mapOf("A" to Mapping.ROCK, "B" to Mapping.PAPER, "C" to Mapping.SCISSORS) as HashMap<String, Mapping>
val PlayerMapping: HashMap<String, Mapping> =
    mapOf("X" to Mapping.ROCK, "Y" to Mapping.PAPER, "Z" to Mapping.SCISSORS) as HashMap<String, Mapping>

enum class Mapping {
    ROCK,
    PAPER,
    SCISSORS
}