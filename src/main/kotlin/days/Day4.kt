package days

import java.io.File

class Day4 {
    fun day4(){
        val fileURL = "src/main/resources/Day4/input.txt"
        val file = File(fileURL)
        val elfPairs = mutableListOf<ElfPair>()
        file.forEachLine {  line: String ->
            val bothElves = line.split(",")
            val firstElfUnparsed = bothElves[0].split("-")
            val secondElfUnparsed = bothElves[1].split("-")
            val firstElf = Elf(firstElfUnparsed[0].toInt()..firstElfUnparsed[1].toInt())
            val secondElf = Elf(secondElfUnparsed[0].toInt()..secondElfUnparsed[1].toInt())
            val elfPair = ElfPair(firstElf, secondElf)
            elfPairs.add(elfPair)
        }
        println("There are ${elfPairs.count { it.isFullyContained }} elf pairs that fully contain themselves")
        println("There are ${elfPairs.count { it.isPartiallyContained }} elf pairs that partially contain themselves")
    }
    companion object{
        class Elf(val section: IntRange)
        class ElfPair(val firstElf: Elf, val secondElf: Elf){
            val isFullyContained: Boolean = firstElf.section.contains(secondElf.section) || secondElf.section.contains(firstElf.section)
            val isPartiallyContained: Boolean = firstElf.section.partiallyContains(secondElf.section) || secondElf.section.partiallyContains(firstElf.section)
        }
        fun IntRange.contains(other: IntRange): Boolean{
            return other.all { this.contains(it) }
        }
        fun IntRange.partiallyContains(other: IntRange): Boolean{
            return other.any { this.contains(it) }
        }
    }
}