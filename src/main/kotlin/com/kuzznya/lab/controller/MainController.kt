package com.kuzznya.lab.controller

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Vector
import com.kuzznya.lab.physics.model.Block
import com.kuzznya.lab.service.ModelEngine
import com.kuzznya.lab.view.ResizableCanvas
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import kotlinx.coroutines.*

class MainController {
    @FXML
    private lateinit var m1Input: TextField
    @FXML
    private lateinit var m2Input: TextField
    @FXML
    private lateinit var v1xInput: TextField
    @FXML
    private lateinit var v1yInput: TextField
    @FXML
    private lateinit var v2xInput: TextField
    @FXML
    private lateinit var v2yInput: TextField
    @FXML
    private lateinit var x1Input: TextField
    @FXML
    private lateinit var y1Input: TextField
    @FXML
    private lateinit var x2Input: TextField
    @FXML
    private lateinit var y2Input: TextField
    @FXML
    private lateinit var w1Input: TextField
    @FXML
    private lateinit var h1Input: TextField
    @FXML
    private lateinit var w2Input: TextField
    @FXML
    private lateinit var h2Input: TextField
    @FXML
    private lateinit var isElasticBox: CheckBox
    @FXML
    private lateinit var canvasPane: AnchorPane

    private var engine: ModelEngine? = null

    private var m1: Double = 0.0
    private var m2: Double = 0.0
    private var v1x: Double = 0.0
    private var v1y: Double = 0.0
    private var v2x: Double = 0.0
    private var v2y: Double = 0.0
    private var x1: Double = 0.0
    private var y1: Double = 0.0
    private var x2: Double = 0.0
    private var y2: Double = 0.0
    private var w1: Double = 0.0
    private var h1: Double = 0.0
    private var w2: Double = 0.0
    private var h2: Double = 0.0

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
        v1xInput.setOnAction { event: ActionEvent -> v1x = commitOrRevert(event.source as TextField, v1x) }
        v1yInput.setOnAction { event: ActionEvent -> v1y = commitOrRevert(event.source as TextField, v1y) }
        v2xInput.setOnAction { event: ActionEvent -> v2x = commitOrRevert(event.source as TextField, v2x) }
        v2yInput.setOnAction { event: ActionEvent -> v2y = commitOrRevert(event.source as TextField, v2y) }
        x1Input.setOnAction { event: ActionEvent -> x1 = commitOrRevert(event.source as TextField, x1) }
        y1Input.setOnAction { event: ActionEvent -> y1 = commitOrRevert(event.source as TextField, y1) }
        x2Input.setOnAction { event: ActionEvent -> x2 = commitOrRevert(event.source as TextField, x2) }
        y2Input.setOnAction { event: ActionEvent -> y2 = commitOrRevert(event.source as TextField, y2) }
        w1Input.setOnAction { event: ActionEvent -> w1 = commitOrRevert(event.source as TextField, w1) }
        h1Input.setOnAction { event: ActionEvent -> h1 = commitOrRevert(event.source as TextField, h1) }
        w2Input.setOnAction { event: ActionEvent -> w2 = commitOrRevert(event.source as TextField, w2) }
        h2Input.setOnAction { event: ActionEvent -> h2 = commitOrRevert(event.source as TextField, h2) }
    }

    private fun updateParams() {
        m1 = commitOrRevert(m1Input, m1)
        m2 = commitOrRevert(m2Input, m2)
        v1x = commitOrRevert(v1xInput, v1x)
        v1y = commitOrRevert(v1yInput, v1y)
        v2x = commitOrRevert(v2xInput, v2x)
        v2y = commitOrRevert(v2yInput, v2y)
        x1 = commitOrRevert(x1Input, x1)
        y1 = commitOrRevert(y1Input, y1)
        x2 = commitOrRevert(x2Input, x2)
        y2 = commitOrRevert(y2Input, y2)
        w1 = commitOrRevert(w1Input, w1)
        h1 = commitOrRevert(h1Input, h1)
        w2 = commitOrRevert(w2Input, w2)
        h2 = commitOrRevert(h2Input, h2)
    }

    @FXML
    private fun start() = runBlocking {
        engine?.let { cancelComputation() }

        updateParams()

        val canvas: Canvas = ResizableCanvas()
        canvasPane.children.add(canvas)
        canvas.widthProperty().bind(canvasPane.widthProperty())
        canvas.heightProperty().bind(canvasPane.heightProperty())

        engine = ModelEngine(
            listOf(
                Block(
                    m1,
                    Point(x1, y1),
                    Vector(v1x, v1y),
                    w1,
                    h1,
                    Color.DARKBLUE
                ),
                Block(
                    m2,
                    Point(x2, y2),
                    Vector(v2x, v2y),
                    w2,
                    h2,
                    Color.DARKRED
                )
            ),
            canvas)
        engine?.computation?.start()
    }

    @FXML
    private fun cancelComputation() = runBlocking {
        engine?.computation?.cancel()
        canvasPane.children.clear()
    }
}