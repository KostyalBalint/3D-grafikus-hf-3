package gameObjects.car

import CarMoveStrategy
import GameObject
import WebGL2RenderingContext
import move.WheelMoveStrategy
import vision.gears.webglmath.Vec3
import kotlin.math.PI

class Car(gl: WebGL2RenderingContext) {

    private var carBody = CarBody(gl)
    private var frontRightWheel = Wheel(gl)
    private var frontLeftWheel = Wheel(gl)
    private var backRightWheel = Wheel(gl)
    private var backLeftWheel = Wheel(gl)

    public val backward
    get() = Vec3(0f, 0f, 1f).rotateY(carBody.yaw + PI.toFloat())

    init {
        frontRightWheel.apply{
            parent = carBody
            position.set(Vec3(7.0f, -3.5f, 14.0f))
            move = WheelMoveStrategy(true)
        }
        frontLeftWheel.apply{
            parent = carBody
            position.set(Vec3(-7.0f, -3.5f, 14.0f))
            move = WheelMoveStrategy(true)
        }
        backRightWheel.apply{
            parent = carBody
            position.set(Vec3(7.0f, -3.5f, -11.0f))
            move = WheelMoveStrategy()
        }
        backLeftWheel.apply{
            parent = carBody
            position.set(Vec3(-7.0f, -3.5f, -11.0f))
            move = WheelMoveStrategy()
        }

        carBody.apply{
            inverseMass = 1.0f / 100.0f
            position.y = 7f
            move = CarMoveStrategy()
        }
    }

    fun getPosition(): Vec3 {
        return carBody.position
    }
    fun getGameObjects(): Array<GameObject> {
        return arrayOf(carBody, frontLeftWheel, frontRightWheel, backRightWheel, backLeftWheel)
    }
}