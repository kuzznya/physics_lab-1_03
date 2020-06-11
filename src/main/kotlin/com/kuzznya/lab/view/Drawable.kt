package com.kuzznya.lab.view

import com.kuzznya.lab.model.Scale
import javafx.scene.canvas.GraphicsContext

interface Drawable {
    fun render(ctx: GraphicsContext, scale: Scale)
}