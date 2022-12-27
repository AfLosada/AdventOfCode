package days


import java.io.File
import java.util.Collections
import java.util.PriorityQueue

class Day8 {

    private lateinit var listOfRows: Array<IntArray>
    private lateinit var grid: Grid
    private lateinit var visibleTrees: MutableSet<Tree>

    init {
        day8()
    }

    private fun day8() {
        val fileURL = "src/main/resources/Day8/input.txt"
        val file = File(fileURL)
        file.readLines().forEachIndexed { indexOfRow, line ->
            if (!this::listOfRows.isInitialized) {
                val length = line.length
                listOfRows = Array(length) { IntArray(length) }
            }
            line.forEachIndexed { indexOfTree, tree -> listOfRows[indexOfRow][indexOfTree] = tree.toString().toInt() }
        }
        grid = Grid(listOfRows)
        visibleTrees = mutableSetOf()
    }

    fun part1() {
        listOfRows.forEachIndexed { index, treeArray ->
            findVisibleTreesInRow(treeArray, index)
            findVisibleTreesInRow(treeArray.reversedArray(), index, true)
        }
        grid.listOfColumns.forEachIndexed { index, treeArray ->
            findVisibleTreesInColumn(treeArray, index)
            findVisibleTreesInColumn(treeArray.reversedArray(), index, true)
        }
        println("There are ${visibleTrees.size} trees visible from outside")
        part2()
    }

    fun part2() {
        val priorityQueue: PriorityQueue<Tree> = PriorityQueue(Collections.reverseOrder())
        visibleTrees.forEach { priorityQueue.add(it) }
        println("The tree with the highest possible scenic order has: ${priorityQueue.peek().scenicScore()}")
    }

    private fun findVisibleTreesInRow(row: IntArray, rowIndex: Int, isReversed: Boolean = false) {
        var tallestTreeBehind = -1
        row.forEachIndexed { columnIndex: Int, height: Int ->
            if (height > tallestTreeBehind) {
                tallestTreeBehind = height
                val columnIndexParsed = run {
                    if (isReversed) {
                        row.size - (columnIndex + 1)
                    } else {
                        columnIndex
                    }
                }
                visibleTrees.add(Tree(rowIndex, columnIndexParsed, height, grid))
            }
        }
    }

    private fun findVisibleTreesInColumn(column: IntArray, columnIndex: Int, isReversed: Boolean = false) {
        var tallestTreeBehind = -1
        column.forEachIndexed { rowIndex: Int, height: Int ->
            if (height > tallestTreeBehind) {
                tallestTreeBehind = height
                val rowIndexParsed = run {
                    if (isReversed) {
                        column.size - (rowIndex + 1)
                    } else {
                        rowIndex
                    }
                }
                visibleTrees.add(Tree(rowIndexParsed, columnIndex, height, grid))
            }
        }
    }


    companion object {
        class Tree(val x: Int, val y: Int, val height: Int, private val grid: Grid) : Comparable<Tree> {

            fun scenicScore(): Int {
                val upRange = this.y - 1 downTo 0
                val downRange = this.y + 1 until grid.listOfRows.size
                val leftRange = this.x - 1 downTo 0
                val rightRange = this.x + 1 until grid.listOfRows.size
                val treesUp = filterTreeViewColumn(upRange)
                val treesDown = filterTreeViewColumn(downRange)
                val treesLeft = filterTreeViewRow(leftRange)
                val treesRight = filterTreeViewRow(rightRange)

                return (treesUp.size * treesDown.size * treesLeft.size * treesRight.size)
            }

            private fun filterTreeViewRow(rangeToCheck: IntProgression): List<Int> {
                val listOfTrees: MutableList<Int> = mutableListOf()
                run breaking@{
                    rangeToCheck.forEach { i: Int ->
                        val currentTreeSeen = grid.listOfRows[i][this.y]
                        if (this.height <= currentTreeSeen) {
                            listOfTrees.add(currentTreeSeen)
                            return@breaking
                        }
                        listOfTrees.add(currentTreeSeen)
                    }
                }
                return listOfTrees
            }

            private fun filterTreeViewColumn(rangeToCheck: IntProgression): List<Int> {
                val listOfTrees: MutableList<Int> = mutableListOf()
                run breaking@{
                    rangeToCheck.forEach { j: Int ->
                        val currentTreeSeen = grid.listOfRows[this.x][j]
                        if (this.height <= currentTreeSeen) {
                            listOfTrees.add(currentTreeSeen)
                            return@breaking
                        }
                        listOfTrees.add(currentTreeSeen)
                    }
                }
                return listOfTrees
            }

            override fun compareTo(other: Tree): Int {
                return this.scenicScore().compareTo(other.scenicScore())
            }

            override fun equals(other: Any?): Boolean {
                return (other is Tree) && x == other.x && y == other.y && height == other.height
            }

            override fun hashCode(): Int {
                return "$x$y$height".hashCode()
            }
        }

        class Grid(val listOfRows: Array<IntArray>) {
            val listOfColumns = run {
                val list = Array(listOfRows.size) { IntArray(listOfRows.size) }
                for (j in IntRange(0, listOfRows.size - 1)) {
                    for (i in IntRange(0, listOfRows.size - 1)) {
                        val currentTree = listOfRows[i][j]
                        list[j][i] = currentTree
                    }
                }
                list
            }
        }
    }
}