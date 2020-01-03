package com.lm.tm

import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

object WindowUtils {

    fun createLoadPathPicker(): File {
        val fileChooser = FileChooser()
        return fileChooser.showOpenDialog(Stage())
    }
}