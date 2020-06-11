package com.kuzznya.lab.model

import com.kuzznya.lab.view.Drawable

abstract class DrawablePhysObject (
    mass: Double,
    position: Point,
    speed: Vector
) : PhysObject(mass, position, speed), Drawable