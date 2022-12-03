package days

import java.io.File

class Day2 {
    fun day2() {
        var sum = 0
        val fileURL = "src/main/resources/Day2/input.txt"
        val file = File(fileURL)
        file.forEachLine { line: String ->
            val match = line.split(" ")
            val enemy = EnemyMapping[match[0]]!!
            val player = PlayerMapping[match[1]]!!
            val pointsPerMatch = calculatePointsPerMatch(player)
            val playerPlay = calculatePlayerMappingPerMatch(enemy, player)
            val pointsPerPlay = calculatePointsPerPlay(playerPlay)
            sum += pointsPerMatch + pointsPerPlay
        }
        println("You will obtain $sum if everything goes as expected")
    }



    companion object {
        fun calculatePointsPerPlay(play: Enemy): Int {
            return when (play) {
                Enemy.ROCK -> 1
                Enemy.PAPER -> 2
                Enemy.SCISSORS -> 3
            }
        }

        val PlayerMapping: HashMap<String, Player> =
            mapOf("X" to Player.LOSE, "Y" to Player.DRAW, "Z" to Player.WIN) as HashMap<String, Player>
        val EnemyMapping: HashMap<String, Enemy> =
            mapOf("A" to Enemy.ROCK, "B" to Enemy.PAPER, "C" to Enemy.SCISSORS) as HashMap<String, Enemy>

        fun calculatePointsPerMatch(me: Player): Int {
            return when (me) {
                Player.LOSE -> 0
                Player.DRAW -> 3
                Player.WIN -> 6
            }
        }

        fun calculatePlayerMappingPerMatch(enemy: Enemy, me: Player): Enemy {
            return when(enemy){
                Enemy.PAPER -> {
                    when(me) {
                        Player.DRAW -> Enemy.PAPER
                        Player.LOSE -> Enemy.ROCK
                        Player.WIN -> Enemy.SCISSORS
                    }
                }
                Enemy.SCISSORS -> {
                    when(me) {
                        Player.DRAW -> Enemy.SCISSORS
                        Player.LOSE -> Enemy.PAPER
                        Player.WIN -> Enemy.ROCK
                    }
                }
                Enemy.ROCK -> {
                    when(me) {
                        Player.DRAW -> Enemy.ROCK
                        Player.LOSE -> Enemy.SCISSORS
                        Player.WIN -> Enemy.PAPER
                    }
                }
            }
        }

    }
}

enum class Enemy {
    ROCK,
    PAPER,
    SCISSORS
}

enum class Player {
    LOSE,
    DRAW,
    WIN
}


