package com.lm.tm.controller

import com.lm.tm.Transition
import com.lm.tm.TuringMachine
import com.lm.tm.WindowUtils
import com.lm.tm.view.MainView
import tornadofx.*
import java.io.File

class MainController : Controller() {

    private val mainView: MainView by inject()
    private lateinit var turingMachine: TuringMachine
    private var alphabet: List<String>
    private var transitionTable: Map<String, Map<String, Transition>>
    val observableTape = listOf<String>().toObservable()

    init {
        alphabet = readTextFromFile("data/alphabet.txt")
        transitionTable = readTransitionTableFromFile("data/tt.txt", alphabet)
    }

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
            TuringMachine(transitionTable, startState, tape, startIndex)
        mainView.selectTapeElement(startIndex)
    }

    private fun isTuringMachineRightOriented() =
        transitionTable.any { tt -> tt.value.any { it.value.direction == "R" } }

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

    fun loadAlphabet() {
        val file = WindowUtils.createLoadPathPicker()
        alphabet = readTextFromFile(file.path)
        mainView.setTextFormatter(alphabet)
    }

    fun loadTransitionTable() {
        val file = WindowUtils.createLoadPathPicker()
        transitionTable = readTransitionTableFromFile(file.path, alphabet)
    }

    private fun readTextFromFile(path: String, delimiter: String = "#") =
        File(path).inputStream().bufferedReader().use {
            it.readText().split(delimiter)
        }

    private fun readTransitionTableFromFile(
        path: String,
        alphabet: List<String>,
        stateDelimiter: String = ",",
        statesDelimiter: String = "#"
    ): Map<String, Map<String, Transition>> {
        val transitionTable =
            mutableMapOf<String, Map<String, Transition>>()
        File(path).inputStream().bufferedReader().useLines { lines ->
            lines.forEach {
                val states = it.split(stateDelimiter)
                val inState = states.first()
                val transitionMap =
                    states.drop(1).mapIndexed { index, transitionParameters ->
                        val transitionList =
                            transitionParameters.split(statesDelimiter)
                        val nextState = transitionList[0]
                        val symbol = transitionList[1]
                        val direction = transitionList[2]
                        val transition =
                            Transition(nextState, symbol, direction)
                        Pair(alphabet[index], transition)
                    }.toMap()
                transitionTable[inState] = transitionMap
            }
        }
        return transitionTable
    }
}