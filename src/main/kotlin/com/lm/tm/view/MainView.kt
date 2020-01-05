package com.lm.tm.view

import com.lm.tm.controller.MainController
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.shape.Circle
import tornadofx.*

class MainView : View("Turing Machine") {

    private val mainController: MainController by inject()
    private var tapeListView: ListView<String> by singleAssign()
    private var inputTextField: TextField by singleAssign()
    private var pathTextArea: TextArea by singleAssign()

    override val root = vbox {
        vboxConstraints {
            alignment = Pos.CENTER
        }
        prefWidth = 500.0
        prefHeight = 450.0
        hbox {
            vboxConstraints {
                alignment = Pos.CENTER
            }
            button {
                hboxConstraints {
                    margin = Insets(25.0, 10.0, 25.0, 0.0)
                }
                prefWidth = 200.0
                text = "LOAD TRANSITION TABLE"
                setOnMouseClicked {
                    mainController.loadTransitionTable()
                }
            }
            button {
                hboxConstraints {
                    margin = Insets(25.0, 0.0, 25.0, 10.0)
                }
                prefWidth = 200.0
                text = "LOAD ALPHABET"
                setOnMouseClicked {
                    mainController.loadAlphabet()
                }
            }
        }
        hbox {
            vboxConstraints {
                alignment = Pos.CENTER
            }
            label {
                hboxConstraints {
                    margin = Insets(25.0, 10.0, 25.0, 0.0)
                }
                text = "Input"
            }
            textfield {
                inputTextField = this
                hboxConstraints {
                    margin = Insets(25.0, 0.0, 25.0, 10.0)
                }
                textFormatter = getAlphabetTextFormatter(listOf("a", "b", "-"))
                prefWidth = 300.0
            }
        }
        button {
            vboxConstraints {
                margin = Insets(10.0, 25.0, 10.0, 25.0)
            }
            prefWidth = 450.0
            text = "START PROCESSING"
            setOnMouseClicked {
                val newInput = inputTextField.text.chunked(1)
                mainController.startProcessing(newInput)
            }
        }
        listview(mainController.observableTape) {
            maxWidth = 450.0
            prefHeight = 40.0
            vboxConstraints {
                margin = Insets(10.0, 25.0, 10.0, 25.0)
            }
            tapeListView = this
            selectionModel.selectionMode = SelectionMode.SINGLE
            orientation = Orientation.HORIZONTAL
            isMouseTransparent = true
            isFocusTraversable = false
            selectionModel.select(0)
        }
        button {
            vboxConstraints {
                margin = Insets(10.0, 25.0, 10.0, 25.0)
            }
            val radius = 30.0
            shape = Circle(radius)
            setMinSize(2 * radius, 2 * radius)
            setMaxSize(2 * radius, 2 * radius)
            text = "NEXT"
            setOnMouseClicked {
                mainController.processNextSymbol()
            }
        }
        textarea {
            vboxConstraints {
                margin = Insets(10.0, 25.0, 10.0, 25.0)
            }
            pathTextArea = this
            prefHeight = 40.0
            maxWidth = 450.0
        }
    }

    private fun getAlphabetTextFormatter(alphabet: List<String>) =
        object : TextFormatter<String>({
            val newText = it.text
            when {
                newText.isEmpty() -> it
                !alphabet.any { a -> newText.contains(a) } -> null
                else -> it
            }
        }) {}

    fun selectTapeElement(index: Int) {
        tapeListView.selectionModel.select(index)
    }

    fun showPath(pathString: String) {
        pathTextArea.text = pathString
    }

    fun setTextFormatter(alphabet: List<String>) {
        inputTextField.textFormatter = getAlphabetTextFormatter(alphabet)
    }

    fun clearTextArea() {
        pathTextArea.clear()
    }
}