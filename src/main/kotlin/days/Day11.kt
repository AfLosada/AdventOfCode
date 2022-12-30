package days

import java.io.File
import java.math.BigInteger

class Day11 {

    var monkeyList: List<Monkey>
    var worryDivisor = 1

    init {
        val fileURL = "src/main/resources/Day11/input.txt"
        val file = File(fileURL)
        val monkeyListToParse = file.readLines().filter { it.isNotEmpty() }.chunked(6)
        monkeyList = monkeyListToParse.mapIndexed { index, monkey ->
            val itemList =
                monkey[1].filterNot { it == ',' }.split(" ").filter { it.toIntOrNull() != null }.map { it.toBigInteger() }
                    .toMutableList()
            val operationStringSplit = monkey[2].split(" ")
            val operation = createOperationBasedOnOperator(
                operationStringSplit[operationStringSplit.size - 2],
                operationStringSplit.last()
            )
            val testDivisor = monkey[3].split(" ").last().toInt()
            fun test(item: BigInteger): Int {
                return if (item % testDivisor.toBigInteger() == BigInteger.ZERO) {
                    println("Current worry level is divisible by $testDivisor")
                    monkey[4].split(" ").last().toInt()
                } else {
                    println("Current worry level is not divisible by $testDivisor")
                    monkey[5].split(" ").last().toInt()
                }
            }
            Monkey(index, itemList, operation, ::test, testDivisor)
        }
        println(monkeyList)
    }

    fun part1() {
        worryDivisor = 3
        for (i in 1..20) {
            executeRound()
        }
        val sortedList = monkeyList.sortedDescending()
        val firstMonkey = sortedList[0]
        val secondMonkey = sortedList[1]
        println("Monkey ${firstMonkey.id} inspected ${firstMonkey.inspections}")
        println("Monkey ${secondMonkey.id} inspected ${secondMonkey.inspections}")
        println("Monkey business is: ${firstMonkey.inspections * secondMonkey.inspections}")
    }

    fun part2() {
        //Modify the worryDivisor
        worryDivisor = lcm(monkeyList.map { it.divisor })
        for (i in 1..10000) {
            executeRound()
        }
        val sortedList = monkeyList.sortedDescending()
        val firstMonkey = sortedList[0]
        val secondMonkey = sortedList[1]
        println("Monkey ${firstMonkey.id} inspected ${firstMonkey.inspections}")
        println("Monkey ${secondMonkey.id} inspected ${secondMonkey.inspections}")
        println("Monkey business is: ${(firstMonkey.inspections.toBigInteger() * secondMonkey.inspections.toBigInteger())}")
    }

    fun executeRound() {
        val listIterator = monkeyList.iterator()
        while (listIterator.hasNext()) {
            val monkey = listIterator.next()
            println("Monkey: ${monkey.id}")
            val temporalMonkeyItemList = monkey.itemList.toMutableList()
            monkey.itemList.forEachIndexed { index, item ->
                monkey.inspections++
                println("Monkey inspects an item with a worry level of ${item}")
                val worryLevelDuringInspection = monkey.operation(item)
                println("Worry level is $worryLevelDuringInspection")
                val worryLevelAfterInspection = worryLevelDuringInspection % worryDivisor.toBigInteger()
                println("Monkey gets bored with item. Worry level is $worryLevelAfterInspection")
                val monkeyToPassTo = monkey.test(worryLevelAfterInspection)
                println("Monkey gets bored with item. Worry level is $worryLevelAfterInspection")
                temporalMonkeyItemList.remove(item)
                monkeyList[monkeyToPassTo].itemList.add(worryLevelAfterInspection)
                println("Item with worry level $worryLevelAfterInspection is thrown to monkey $monkeyToPassTo.")
            }
            monkey.itemList = temporalMonkeyItemList
        }
    }

    fun createOperationBasedOnOperator(operator: String, value: String): (old: BigInteger) -> BigInteger {
        fun add(old: BigInteger): BigInteger {
            return if (value != "old") {
                old + value.toBigInteger()
            } else {
                old + old
            }
        }

        fun substract(old: BigInteger): BigInteger {
            return if (value != "old") {
                old - value.toBigInteger()
            } else {
                old - old
            }
        }

        fun multiply(old: BigInteger): BigInteger {
            return if (value != "old") {
                old * value.toBigInteger()
            } else {
                old * old
            }
        }

        fun divide(old: BigInteger): BigInteger {
            return if (value != "old") {
                old / value.toBigInteger()
            } else {
                old / old
            }
        }

        fun none(old: BigInteger): BigInteger {
            return if (value != "old") {
                old
            } else {
                old
            }
        }
        return when (operator) {
            "+" -> ::add
            "-" -> ::substract
            "*" -> ::multiply
            "/" -> ::divide
            else -> ::none
        }
    }

    fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    fun lcm(numbers: List<Int>): Int {
        var lcm = numbers[0]
        for (i in 1 until numbers.size) {
            lcm = lcm * numbers[i] / gcd(lcm, numbers[i])
        }
        return lcm
    }

    companion object {
        class Monkey(
            val id: Int,
            var itemList: MutableList<BigInteger>,
            val operation: (old: BigInteger) -> BigInteger,
            val test: (value: BigInteger) -> Int,
            val divisor: Int,
        ) :
            Comparable<Monkey> {
            var inspections = 0
            override fun compareTo(other: Monkey): Int {
                return this.inspections.compareTo(other.inspections)
            }
        }
    }
}