package days

import java.io.File

class Day6 {
    fun day6(){
        val fileURL = "src/main/resources/Day6/input.txt"
        val file = File(fileURL)
        val lastBytesForPacket = ArrayDeque<Byte>()
        val lastBytesForMessage = ArrayDeque<Byte>()
        var lastPacketIndex = 0
        var lastMessageIndex = 0
        run lit@{
            file.readBytes().forEachIndexed { index, byte ->
                if(lastPacketIndex == 0){
                    if (lastBytesForPacket.size == 4) {
                        lastBytesForPacket.removeFirst()
                    }
                    if (lastBytesForPacket.size == 3) {
                        val tempBytes = ArrayDeque<Byte>()
                        tempBytes.addAll(lastBytesForPacket)
                        tempBytes.add(byte)
                        if (!tempBytes.hasDuplicates()) {
                            lastPacketIndex = index
                        }
                    }
                    lastBytesForPacket.addLast(byte)
                }
                if(lastBytesForMessage.size == 14){
                    lastBytesForMessage.removeFirst()
                }
                if (lastBytesForMessage.size == 13){
                    val tempBytes = ArrayDeque<Byte>()
                    tempBytes.addAll(lastBytesForMessage)
                    tempBytes.add(byte)
                    if (!tempBytes.hasDuplicates()) {
                        lastMessageIndex = index
                        return@lit
                    }
                }
                lastBytesForMessage.addLast(byte)
            }
        }
        lastPacketIndex++
        println("Last packet index is: $lastPacketIndex")
        lastMessageIndex++
        println("Last message index is: $lastMessageIndex")
    }
    private fun ArrayDeque<Byte>.hasDuplicates(): Boolean{
        return this.any { toCheck -> this.filter { it == toCheck }.size > 1 }
    }
}