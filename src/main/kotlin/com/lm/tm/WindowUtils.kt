package com.lm.tm

import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

object WindowUtils {

    fun createLoadDirectoryPathPicker(): File {
        val directoryChooser = DirectoryChooser()
        return directoryChooser.showDialog(Stage())
    }

    fun createLoadPathPicker(): File {
        val fileChooser = FileChooser()
        return fileChooser.showOpenDialog(Stage())
    }
}