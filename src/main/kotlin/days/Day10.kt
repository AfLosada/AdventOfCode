package days

import java.io.File

class Day10 {
    lateinit var currentLine: String
    lateinit var currentLineSplit: List<String>
    var currentCommand: String = ""
    lateinit var currentNumber: String
    var iterationsToSkip: Int = 1
    val listOfSignals = mutableListOf<Pair<Int, Int>>()
    fun part1() {
        var hasFinished = false
        val fileURL = "src/main/resources/Day10/input.txt"
        val file = File(fileURL)
        val lines = file.readLines()
        var x = 1
        var iteration = 0
        var lineToRead = 0
        var printScreen = ""
        while (!hasFinished) {
            if (iterationsToSkip == 0) {
                if (currentCommand == "addx") {
                    //println("Add $currentNumber to X")
                    x += currentNumber.toInt()
                }
                hasFinished = lineToRead == lines.size
                if(!hasFinished){
                    currentLine = lines[lineToRead++]
                }
                readCommand()
            }
            if(iteration in listOf(20,60,100,140,180,220)){
                listOfSignals.add(Pair(iteration, x))
            }
            val cycleModuleScreenSize = (iteration % 40) -1
            val rangeAvailable = listOf(cycleModuleScreenSize - 1, cycleModuleScreenSize, cycleModuleScreenSize + 1)
            printScreen += if(x in rangeAvailable) "#" else "."
            if (printScreen.length == 40) {
                println(printScreen)
                printScreen = ""
            }
            iteration++
            //println("During iteration: $iteration, x has a value of $x. There are ${iterationsToSkip} iterations to skip.")
            iterationsToSkip--
        }
        val sumOfSignalStrengths = listOfSignals.sumOf { it.first * it.second }
        println("The sum of all signal strengths is $sumOfSignalStrengths")
        printScreen.chunked(40).forEach { println(it) }
    }

    fun readCommand() {
        currentLineSplit = currentLine.split(" ")
        currentCommand = currentLineSplit[0]
        currentNumber = currentLineSplit.getOrNull(1) ?: ""
        iterationsToSkip = when (currentCommand) {
            "noop" -> 1
            "addx" -> 2
            else -> 1
        }
    }
}