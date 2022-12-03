package days

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class Day1 {
    fun day1()
    {
        val fileURL = "src/main/resources/Day1/input.txt"
        val file = File(fileURL)
        val priorityQueue: PriorityQueue<Elf> = PriorityQueue<Elf>( Collections.reverseOrder() )
        var currentCandyList: MutableList<Int> = mutableListOf()
        file.forEachLine { line: String ->
            if (line.isEmpty()) {
                priorityQueue.add(Elf(currentCandyList))
                currentCandyList = ArrayList()
            } else {
                currentCandyList.add(line.toInt())
            }
        }
        val firstElf = priorityQueue.remove()
        val secondElf = priorityQueue.remove()
        val thirdElf = priorityQueue.remove()
        println("The first elf is ${firstElf.candyList.sum()}")
        println("The second elf is ${secondElf.candyList.sum()}")
        println("The third elf is ${thirdElf.candyList.sum()}")
        println("The total sum is: ${listOf(firstElf.candyList.sum(), secondElf.candyList.sum(), thirdElf.candyList.sum()).sum()}")
    }

    class Elf(val candyList: List<Int> = ArrayList()) : Comparable<Elf>{
        override fun compareTo(other: Elf): Int {
            return this.candyList.sum().compareTo(other.candyList.sum())
        }
    }

}