import colliders.BoxCollider
import gameObjects.car.Car
import gameObjects.car.CarBody
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec4
import kotlin.math.*

fun GameObject.CarMoveStrategy() =
    object : GameObject.Motion(this) {
        var wheelBase = 20f // Distance between the front and rear wheels
        var steeringAngle = 15f // Amount to turn in degrees
        var enginePower = 400f // Acceleration rate
        var friction = -0.95f
        var drag = -0.0100f
        var braking = -250f
        var max_speed_reverse = 50f

        var transform = Vec3()
        var velocity = Vec3()
        var steerDirection = 0.0f
        var acceleration = Vec3()

        override operator fun invoke(
            dt: Float,
            t: Float,
            keysPressed: Set<String>,
            gameObjects: List<GameObject>
        ): Boolean {
            transform = Vec3(sin(yaw), 0f, cos(yaw))

            getInput(keysPressed)
            calculateSteering(dt)
            applyFriction()
            velocity = velocity.plus(acceleration * dt)
            position.set(position + velocity * dt)

            collider = BoxCollider((modelMatrix * Vec4(-7f, -8f, -25f)).xyz, (modelMatrix * Vec4(7f, 12f, 20f)).xyz)

            return true
        }

        private fun applyFriction() {
            if(velocity.length() < .5f) {
                velocity = Vec3()
            }
            var frictionForce = velocity * friction
            val dragForce = velocity * velocity.length() * drag
            if(velocity.length() < 100f) frictionForce = frictionForce * 3f
            acceleration = acceleration + dragForce + frictionForce
        }

        private fun getInput(keysPressed: Set<String>) {
            var turn = 0.0f
            if ("ArrowRight" in keysPressed) turn -= 1.0f
            if ("ArrowLeft" in keysPressed) turn += 1.0f
            steerDirection = turn * deg2rad(steeringAngle)

            acceleration = Vec3()
            if ("ArrowUp" in keysPressed) {
                acceleration = transform * enginePower
            }
            if ("ArrowDown" in keysPressed) {
                acceleration = transform * braking
            }
        }

        private fun calculateSteering(dt: Float) {
            var rearWheel = position - transform
            var frontWheel = position + transform * wheelBase
            rearWheel = rearWheel + velocity * dt
            frontWheel = frontWheel + velocity.rotateY(steerDirection) * dt
            val newHeading = (frontWheel - rearWheel).normalize()

            val d = newHeading.dot(velocity.clone().normalize())
            if(d >= 0f) {
                velocity = newHeading * velocity.length()
                speed = velocity.length() //Only needed for the wheel move visualisation
            }
            if(d < 0f) {
                velocity = -newHeading * min(velocity.length(), max_speed_reverse)
                speed = -velocity.length() //Only needed for the wheel move visualisation
            }

            yaw = atan2(newHeading.x, newHeading.z)
        }

        private fun rad2deg(value: Float): Float {
            return (value * 180.0f / PI).toFloat()
        }

        private fun deg2rad(value: Float): Float {
            return (value * PI / 180).toFloat()
        }
    }
