package com.lm.tm.app

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    init {
        label {
            padding = box(10.px)
            fontWeight = FontWeight.BOLD
        }
        textField {
            padding = box(10.px)
        }
    }
}