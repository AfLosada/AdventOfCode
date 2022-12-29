package days

import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Day9 {

    lateinit var CurrentHeadPosition: Point
    lateinit var CurrentTailPosition: Point
    val body: MutableList<Point> = mutableListOf()
    val pointsTouchedByTail: MutableSet<Point> = mutableSetOf(Point(0, 0))

    private fun move(movement: String) {
        val splitMovement = movement.split(" ")
        val direction = splitMovement[0]
        val distance = splitMovement[1].toInt()
        for (i in 1..distance) {
            moveStepInDirection(direction)
        }
    }

    fun moveBody(directionPoint: Point, lastHeadPosition: Point) {
        if (body.isEmpty()) {
            CurrentHeadPosition += directionPoint
            if (CurrentTailPosition.distanceTo(CurrentHeadPosition) > sqrt(2.0)) {
                CurrentTailPosition = lastHeadPosition
                pointsTouchedByTail.add(CurrentTailPosition)
            }
        } else {
            CurrentHeadPosition += directionPoint
            var lastUpdatedPosition = CurrentHeadPosition
            var lastPosition = lastHeadPosition
            body.forEachIndexed { index, knot ->
                if (knot.distanceTo(lastUpdatedPosition) > sqrt(2.0)) {
                    val normalizedDirection = (lastUpdatedPosition - body[index]).normalize()
                    val temporalLastPosition = body[index]
                    body[index] += normalizedDirection
                    lastPosition = temporalLastPosition
                    if(index == body.size-1){
                        pointsTouchedByTail.add(lastPosition)
                    }
                }
                lastUpdatedPosition = body[index]
            }
        }
    }

    fun moveStepInDirection(direction: String) {
        when (direction) {
            "R" -> {
                val directionPoint = Point(1, 0)
                moveBody(directionPoint, CurrentHeadPosition)
            }

            "L" -> {
                val directionPoint = Point(-1, 0)
                moveBody(directionPoint, CurrentHeadPosition)
            }

            "U" -> {
                val directionPoint = Point(0, 1)
                moveBody(directionPoint, CurrentHeadPosition)
            }

            "D" -> {
                val directionPoint = Point(0, -1)
                moveBody(directionPoint, CurrentHeadPosition)
            }
        }
    }

    fun part1() {
        CurrentHeadPosition = Point(0, 0)
        CurrentTailPosition = Point(0, 0)
        val fileURL = "src/main/resources/Day9/input.txt"
        val file = File(fileURL)
        file.readLines().forEach { movement ->
            move(movement)
        }
        print("The Tail visits ${pointsTouchedByTail.count()} positions at least once")
    }

    fun part2() {
        CurrentHeadPosition = Point(0, 0)
        body.addAll((0..8).map { Point(0, 0) })
        CurrentTailPosition = body.last()
        val fileURL = "src/main/resources/Day9/input.txt"
        val file = File(fileURL)
        file.readLines().forEach { movement ->
            move(movement)
        }
        print("The Tail visits ${pointsTouchedByTail.count()+1} positions at least once")
    }

    companion object {
        class Point(val x: Int, val y: Int) {
            val position = Pair(x, y)

            fun magnitude(): Double {
                return sqrt(x.toDouble().pow(2.0) + y.toDouble().pow(2.0))
            }

            fun normalize(): Point {
                val roundedX = if(x == -1 || x == 1) x else (x/ magnitude()).roundToInt()
                val roundedY = if(y == -1 || y == 1) y else (y/ magnitude()).roundToInt()
                return Point(roundedX, roundedY)
            }

            override fun equals(other: Any?): Boolean {
                return other is Point && x == other.x && y == other.y
            }

            override fun hashCode(): Int {
                return position.hashCode()
            }
        }

        operator fun Point.minus(other: Point): Point {
            val xDifference = x - other.x
            val yDifference = y - other.y
            return Point(xDifference, yDifference)
        }

        operator fun Point.plus(other: Point): Point {
            val xAddition = x + other.x
            val yAddition = y + other.y
            return Point(xAddition, yAddition)
        }

        fun Point.distanceTo(other: Point): Double {
            val thisPosition = this.position
            val otherPoisition = other.position
            val distanceVector = this - other
            return distanceVector.magnitude()
        }

    }
}