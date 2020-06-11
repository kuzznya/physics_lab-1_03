package com.kuzznya.lab.view

import javafx.scene.canvas.GraphicsContext

interface Drawable {
    fun render(ctx: GraphicsContext)
}