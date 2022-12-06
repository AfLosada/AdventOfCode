package days

import java.io.File
import java.util.Stack
import kotlin.math.pow

class Day5 {
    fun day5() {
        val fileURL = "src/main/resources/Day5/input.txt"
        val file = File(fileURL)
        val linesList = file.readLines()
        val listOfLinesToBeCargo = mutableListOf<String>()
        val listOfMoves = mutableListOf<Move>()
        var amountOfStacks = 0
        var isReadingMoves = false
        linesList.forEach { line ->
            if (line.isEmpty()) {
                isReadingMoves = true
            }
            else if (isReadingMoves) {
                val parsedMoves = line.split(" ").mapNotNull { it.toIntOrNull() }
                listOfMoves.add(
                    Move(
                        parsedMoves[0],
                        parsedMoves[1] - 1,
                        parsedMoves[2] - 1
                    )
                )
            } else if (line.contains("[")) {
                listOfLinesToBeCargo.add(line)
            } else {
                val splitColumns = line.split("  ")
                amountOfStacks = splitColumns.size
            }
        }
        val cargoPane = CargoPane(amountOfStacks)
        setCargoInStacks(cargoPane, listOfLinesToBeCargo.reversed())
        moveCargo(cargoPane, listOfMoves)
        val lastCratesOfStacks = cargoPane.cargoStacks.map { it.removeFirst() }
        println("The last crates of all stacks look like this: ${lastCratesOfStacks.map { it }}")
    }

    private fun setCargoInStacks(cargoPane: CargoPane, listOfLines: List<String>) {
        listOfLines.forEach { line ->
            for (i in 0 until cargoPane.cargoStacks.size) {
                val position = 4*i+1
                val cargo = if (position in line.indices) line[position] else null
                if (cargo != null && cargo.toString().isNotEmpty() && cargo.toString().isNotBlank()) cargoPane.cargoStacks[i].addFirst(cargo.toString())
            }
        }
    }

    private fun moveCargo(cargoPane: CargoPane, listOfMoves: List<Move>) {
        listOfMoves.forEach { move ->
            val temporalStack = ArrayDeque<String>()
            for (i in 0 until move.amountOfMoves) {
                val movedCargo = cargoPane.cargoStacks[move.fromStackNumber].removeFirst()
                temporalStack.addFirst(movedCargo)
            }
            temporalStack.forEach { cargo ->
                cargoPane.cargoStacks[move.toStackNumber].addFirst(cargo)
            }
        }
    }

    companion object {
        class CargoPane(amountOfStacks: Int) {
            val cargoStacks: List<ArrayDeque <String>> = run {
                val emptyList = mutableListOf<ArrayDeque <String>>()
                for (i in 0 until amountOfStacks) {
                    emptyList.add(ArrayDeque())
                }
                emptyList.toList()
            }
        }

        class Move(val amountOfMoves: Int, val fromStackNumber: Int, val toStackNumber: Int)
    }
}