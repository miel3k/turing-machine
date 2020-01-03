package com.lm.tm

class TuringMachine(
    val tape: MutableList<String> = mutableListOf(),
    var currentIndex: Int,
    private val transitionTable: Map<String, Map<String, Triple<String, String, String>>>,
    private var currentState: String,
    val path: MutableList<String> = mutableListOf(),
    var finished: Boolean = false
) {

    fun processNextSymbol() {
        if (path.isEmpty()) path.add(currentState)
        val symbol = tape.getOrNull(currentIndex) ?: run {
            finished = true
            return
        }
        val nextTriple = transitionTable[currentState]?.entries
            ?.find { it.key == symbol }
            ?.value
        val nextState = nextTriple?.first
        val currentValue = nextTriple?.second
        val direction = nextTriple?.third
        tape[currentIndex] = currentValue!!
        when (direction) {
            "R" -> currentIndex += 1
            "L" -> currentIndex -= 1
            else -> {
                finished = true
            }
        }
        path.add(nextState!!)
        currentState = nextState
    }
}