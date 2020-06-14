package com.kuzznya.lab.physics.model

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Vector
import com.kuzznya.lab.view.Drawable

abstract class DrawablePhysObject (
    mass: Double,
    position: Point,
    speed: Vector,
    width: Double,
    height: Double
) : PhysObject(mass, position, speed, width, height), Drawable