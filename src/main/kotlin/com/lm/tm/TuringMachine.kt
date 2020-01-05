package com.lm.tm

class TuringMachine(
    private val transitionTable: Map<String, Map<String, Transition>>,
    private var currentState: String,
    val tape: MutableList<String> = mutableListOf(),
    var currentIndex: Int,
    val path: MutableList<String> = mutableListOf(),
    var finished: Boolean = false
) {

    fun processNextSymbol() {
        if (path.isEmpty()) path.add(currentState)
        val symbol = tape.getOrNull(currentIndex) ?: run {
            finished = true
            return
        }
        val transition = transitionTable[currentState]?.entries
            ?.find { it.key == symbol }
            ?.value!!
        tape[currentIndex] = transition.symbol
        when (transition.direction) {
            "R" -> currentIndex += 1
            "L" -> currentIndex -= 1
            else -> finished = true
        }
        path.add(transition.nextState)
        currentState = transition.nextState
    }
}