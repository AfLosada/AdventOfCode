package days

import java.io.File
import java.lang.management.MemoryType

class Day3 {
    fun day3() {
        val fileURL = "src/main/resources/Day3/input.txt"
        val file = File(fileURL)
        var rucksackList: MutableList<Rucksack> = mutableListOf()
        val elfGroupList: MutableList<ElfGroup> = mutableListOf()
        var count = 0
        file.forEachLine { line ->
            val middleOfString = line.length / 2
            val firstHalf = line.substring(0, middleOfString)
            val secondHalf = line.substring(middleOfString, line.length)
            val rucksack = Rucksack(firstHalf.toItemList(), secondHalf.toItemList())
            rucksackList.add(rucksack)
            if(count == 2){
                val arrayOfList = rucksackList as ArrayList<Rucksack>
                val elfGroup = ElfGroup(arrayOfList[0], arrayOfList[1], arrayOfList[2])
                elfGroupList.add(elfGroup)
                count = 0
                rucksackList = mutableListOf()
            }
            else{
                count++
            }
        }
        elfGroupList.forEach {
            println("The type of this elf group is: ${it.commonType.getValue()}")
        }
        println("The sum of all types of each group is: ${elfGroupList.sumOf { elfGroup: ElfGroup -> elfGroup.commonType.getValue() }}")
    }

    companion object {
        class Item(val char: Char) : Comparable<Item> {
            private var firstAsciiLetter: Char = 'A'
            private var offset: Int = 27
            fun getValue(): Int {
                if (char in ('a'..'z')) {
                    firstAsciiLetter = 'a'
                    offset = 1
                }
                val normalizedChar = char - firstAsciiLetter
                return normalizedChar + offset
            }

            override fun compareTo(other: Item): Int {
                return this.char.compareTo(other.char)
            }
        }

        class Rucksack(firstCompartment: List<Item>, secondCompartment: List<Item>) {
            var commonItems: List<Item> =
               ( firstCompartment.filter { item -> secondCompartment.any { it.char == item.char } }).distinctBy { item -> item.char }
            val rucksack: List<Item> = run {
                val list = mutableListOf<Item>()
                list.addAll(firstCompartment)
                list.addAll(secondCompartment)
                list.toList()
            }
        }

        class ElfGroup(firstRucksack: Rucksack, secondRucksack: Rucksack, thirdRucksack: Rucksack){
            val commonType: Item =
                firstRucksack.rucksack.first { secondRucksack.rucksack.any { item -> item.char == it.char } && thirdRucksack.rucksack.any { item -> item.char == it.char } }
        }

        fun String.toItemList(): List<Item> {
            return this.toCharArray().map { Item(it) }
        }
    }
}

