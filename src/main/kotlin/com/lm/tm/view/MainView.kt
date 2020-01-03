package com.lm.tm.view

import com.lm.tm.controller.MainController
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import tornadofx.*

class MainView : View("TM") {

    private val mainController: MainController by inject()
    private var tapeListView: ListView<String> by singleAssign()
    private var inputTextField: TextField by singleAssign()
    private var pathTextArea: TextArea by singleAssign()

    override val root = vbox {
        hbox {
            button {
                text = "Load Transition Table"
                setOnMouseClicked {
                    mainController.loadTransitionTable()
                }
            }
            textfield {
                inputTextField = this
                textFormatter = getAlphabetTextFormatter()
            }
            button {
                text = "Start processing"
                setOnMouseClicked {
                    val newInput = inputTextField.text.chunked(1)
                    mainController.startProcessing(newInput)
                }
            }
        }
        listview(mainController.observableTape) {
            tapeListView = this
            selectionModel.selectionMode = SelectionMode.SINGLE
            orientation = Orientation.HORIZONTAL
            isMouseTransparent = true
            isFocusTraversable = false
            selectionModel.select(0)
            prefHeight = 40.toDouble()
        }
        button {
            alignment = Pos.CENTER
            text = "Process next symbol"
            setOnMouseClicked {
                mainController.processNextSymbol()
            }
        }
        textarea {
            pathTextArea = this
        }
    }

    private fun getAlphabetTextFormatter() = object : TextFormatter<String>({
        val newText = it.text
        when {
            newText.isEmpty() -> it
            !(newText.contains("a") || newText.contains("b")) -> null
            else -> it
        }
    }) {}

    fun selectTapeElement(index: Int) {
        tapeListView.selectionModel.select(index)
    }

    fun showPath(pathString: String) {
        pathTextArea.text = pathString
    }
}