package com.lm.tm.controller

import com.lm.tm.TM
import com.lm.tm.WindowUtils
import com.lm.tm.view.MainView
import tornadofx.*
import java.io.File

class MainController : Controller() {

    private val mainView: MainView by inject()
    private lateinit var tm: TM
    private lateinit var tt: Map<String, Map<String, Triple<String, String, String>>>

    val entries = listOf<String>().toObservable()

    private var selectedEntryIndex = 0

    fun processNextSymbol() {
        tm.processNextSymbol()
        if (tm.finished) {
            mainView.showPath(getPathString(tm.path))
        } else {
            entries.clear()
            entries.addAll(tm.tape)
            selectedEntryIndex = tm.currentIndex
            mainView.select(selectedEntryIndex)
        }
    }

    fun startProcessing(newEntries: List<String>) {
        entries.clear()
        entries.addAll(newEntries)
        val startIndex = if (tt.any {
                it.value.any {
                    it.value.third == "R"
                }
            }) {
            0
        } else {
            newEntries.size - 1
        }
        tm = TM(newEntries.toMutableList(), startIndex, tt, "q0")
        selectedEntryIndex = startIndex
        mainView.select(selectedEntryIndex)
    }

    private fun getEntriesWithEmptyChars(entries: List<String>): List<String> {
        val list = mutableListOf<String>()
        list.add("-")
        list.addAll(entries)
        list.add("-")
        return list
    }

    private fun getPathString(path: List<String>): String {
        return path.joinToString(" -> ")
    }

    fun loadTransitionTable() {
        val file = WindowUtils.createLoadPathPicker()
        tt = readTransitionTableFromFile(file.path, listOf("a", "b", "-"))
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