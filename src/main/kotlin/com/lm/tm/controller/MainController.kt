package com.lm.tm.controller

import com.lm.tm.TuringMachine
import com.lm.tm.WindowUtils
import com.lm.tm.view.MainView
import tornadofx.*
import java.io.File

class MainController : Controller() {

    private val mainView: MainView by inject()
    private lateinit var turingMachine: TuringMachine
    private lateinit var transitionTable: Map<String, Map<String, Triple<String, String, String>>>
    val observableTape = listOf<String>().toObservable()

    fun processNextSymbol() {
        turingMachine.processNextSymbol()
        mainView.showPath(getPathString(turingMachine.path))
        if (!turingMachine.finished) {
            observableTape.clear()
            observableTape.addAll(turingMachine.tape)
            mainView.selectTapeElement(turingMachine.currentIndex)
        }
    }

    fun startProcessing(input: List<String>) {
        val tape = input.toTape().toMutableList()
        observableTape.clear()
        observableTape.addAll(tape)
        val startIndex =
            if (isTuringMachineRightOriented()) 1 else tape.size - 2
        val startState = transitionTable.keys.first()
        turingMachine =
            TuringMachine(tape, startIndex, transitionTable, startState)
        mainView.selectTapeElement(startIndex)
    }

    private fun isTuringMachineRightOriented() =
        transitionTable.any { tt -> tt.value.any { it.value.third == "R" } }

    private fun List<String>.toTape(): List<String> {
        val list = mutableListOf<String>()
        list.add("-")
        list.addAll(this)
        list.add("-")
        return list
    }

    private fun getPathString(path: List<String>): String {
        return path.joinToString(" -> ")
    }

    fun loadTransitionTable() {
        val file = WindowUtils.createLoadPathPicker()
        val alphabet = listOf("a", "b", "-") //TODO read alphabet from file
        transitionTable = readTransitionTableFromFile(file.path, alphabet)
    }

    private fun readTransitionTableFromFile(
        path: String,
        alphabet: List<String>,
        stateDelimiter: String = ",",
        statesDelimiter: String = "#"
    ): Map<String, Map<String, Triple<String, String, String>>> {
        val transitionTable =
            mutableMapOf<String, Map<String, Triple<String, String, String>>>()
        File(path).inputStream().bufferedReader().useLines { lines ->
            lines.forEach {
                val states = it.split(stateDelimiter)
                val inState = states.first()
                val transitionMap =
                    states.drop(1).mapIndexed { index, outState ->
                        val outList = outState.split(statesDelimiter)
                        Pair(
                            alphabet[index],
                            Triple(outList[0], outList[1], outList[2])
                        )
                    }.toMap()
                transitionTable[inState] = transitionMap
            }
        }
        return transitionTable
    }
}