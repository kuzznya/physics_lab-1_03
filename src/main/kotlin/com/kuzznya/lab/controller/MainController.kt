package com.kuzznya.lab.controller

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Scale
import com.kuzznya.lab.service.ModelEngine
import com.kuzznya.lab.view.ResizableCanvas
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainController {
    @FXML
    private lateinit var m1Input: TextField
    @FXML
    private lateinit var m2Input: TextField
    @FXML
    private lateinit var v1Input: TextField
    @FXML
    private lateinit var v2Input: TextField
    @FXML
    private lateinit var isElasticBox: CheckBox
    @FXML
    private lateinit var canvasPane: AnchorPane

    private var m1: Double = 0.0
    private var m2: Double = 0.0
    private var v1: Double = 0.0
    private var v2: Double = 0.0

    class Mutable (var value: Double)

    private fun commitOrRevert(input: TextField, oldValue: Double) =
        kotlin.runCatching {
            input.text = input.text.toDouble().toString()
            return@runCatching input.text.toDouble()
        } .getOrElse {
            input.text = oldValue.toString()
            return@getOrElse oldValue
        }

    @FXML
    private fun initialize() = runBlocking {
        m1Input.setOnAction { event: ActionEvent -> m1 = commitOrRevert(event.source as TextField, m1) }
        m2Input.setOnAction { event: ActionEvent -> m2 = commitOrRevert(event.source as TextField, m2) }
        v1Input.setOnAction { event: ActionEvent -> v1 = commitOrRevert(event.source as TextField, v1) }
        v2Input.setOnAction { event: ActionEvent -> v2 = commitOrRevert(event.source as TextField, v2) }

        m1 = commitOrRevert(m1Input, m1)
        m2 = commitOrRevert(m2Input, m2)
        v1 = commitOrRevert(v1Input, v1)
        v2 = commitOrRevert(v2Input, v2)

        val canvas: Canvas = ResizableCanvas()
        canvasPane.children.add(canvas)
        canvas.widthProperty().bind(canvasPane.widthProperty())
        canvas.heightProperty().bind(canvasPane.heightProperty())

        val engine = ModelEngine(canvas)
        launch {
            engine.start()
        }
    }
}