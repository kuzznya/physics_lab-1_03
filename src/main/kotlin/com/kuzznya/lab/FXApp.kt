package com.kuzznya.lab

import com.kuzznya.lab.service.Router
import javafx.application.Application
import javafx.stage.Stage

class FXApp : Application() {
    override fun start(primaryStage: Stage) {
        Router.primaryStage = primaryStage
        Router.loadView("Main")
    }
}