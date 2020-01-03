package com.lm.tm.view

import com.lm.tm.controller.MainController
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import tornadofx.*

class MainView : View("TM") {

    private val mainController: MainController by inject()

    private var entryListView: ListView<String> by singleAssign()
    private var inputTextField: TextField by singleAssign()
    private var textArea: TextArea by singleAssign()

    fun select(index: Int) {
        entryListView.selectionModel.select(index)
    }

    override val root = vbox {
        hbox {
            button {
                text = "Load tt"
                setOnMouseClicked {
                    mainController.loadTransitionTable()
                }
            }
            textfield {
                inputTextField = this
                textFormatter = object : TextFormatter<String>({
                    val newText = it.text
                    when {
                        newText.isEmpty() -> it
                        !(newText.contains("a") || newText.contains("b")) -> {
                            null
                        }
                        else -> {
                            it
                        }
                    }
                }) {}
            }
            button {
                text = "Start processing"
                setOnMouseClicked {
                    mainController.startProcessing(inputTextField.text.chunked(1))
                }
            }
        }
        listview(mainController.entries) {
            entryListView = this
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
            textArea = this
            text = ""
        }
    }

    fun showPath(pathString: String) {
        textArea.text = pathString
    }
}