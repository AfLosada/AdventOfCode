package days

import java.io.File

class Day7 {

    private var currentDirectory: Directory? = null
    private var origin: Directory? = null
    private var setOfDirectories = mutableSetOf<Directory>()

    init {
        day7()
    }

    private fun day7() {
        val fileURL = "src/main/resources/Day7/input.txt"
        val file = File(fileURL)
        file.forEachLine { line ->
            if (line.contains("$ cd")) {
                currentDirectory = changeFolder(line)
            } else if (!line.contains("$ ls")) {
                readListedFiles(line)
            }
        }
        origin!!.calculateSizes()
        setOfDirectories.forEach { println("The set ${it.name} has a size of ${it.size}") }
    }

    fun part1() {
        val directoriesToDelete = setOfDirectories.filter { it.size <= 100000 }
        println("The sum of all directories to delete is: ${directoriesToDelete.sumOf { it.size }}")
    }

    fun part2() {
        val diskSize = 70000000
        val minFreeSpace = 30000000
        val smallestDir = setOfDirectories.filter {
            val newSize = origin!!.size - it.size
            val newDifference = diskSize - newSize
            newDifference >= minFreeSpace
        }.minByOrNull { it.size }!!
        println("The smallest directory that, if deleted, would free up enough space has a weight of: ${smallestDir.size}")
    }

    private fun readListedFiles(line: String): File {
        val splitLine = line.split(" ")
        return when (!line.contains("dir")) {
            true -> {
                createFile(splitLine[1], splitLine[0].toInt())
            }

            false -> {
                createDirectory(splitLine[1])
            }
        }
    }

    private fun changeFolder(line: String): Directory {
        return when (val changeFolderArgument = line.split(" ").last()) {
            "/" -> {
                origin = origin ?: createDirectory(changeFolderArgument)
                origin!!
            }

            ".." -> {
                currentDirectory?.parent ?: origin!!
            }

            else -> {
                val newDirectory =
                    currentDirectory?.children?.find { it is Directory && it.name == changeFolderArgument }
                        ?: createDirectory(changeFolderArgument)
                currentDirectory!!.children.add(newDirectory)
                newDirectory as Directory
            }
        }
    }

    private fun createDirectory(name: String): Directory {
        return run {
            val directory = Directory(name, parent = currentDirectory, mutableSetOf())
            directory.parent?.children?.add(directory)
            setOfDirectories.add(directory)
            directory
        }
    }

    private fun createFile(name: String, size: Int): File {
        return run {
            val file = File(name, parent = currentDirectory, size)
            currentDirectory!!.children.add(file)
            file
        }
    }

    companion object {
        class Directory(name: String, parent: Directory?, val children: MutableSet<File>) :
            File(name, parent, children.sumOf { it.size })

        open class File(val name: String, val parent: Directory?, var size: Int) {
            override fun equals(other: Any?): Boolean {
                return (other is File) &&
                        this.name == other.name
            }

            override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + size
                return result
            }
        }

        fun File.calculateSizes(): Int {
            return if (this is Directory && this.size == 0) {
                this.size = this.children.sumOf { it.calculateSizes() }
                size
            } else {
                this.size
            }
        }
    }
}