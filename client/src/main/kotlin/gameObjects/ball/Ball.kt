package gameObjects.car

import GameObject
import JsonLoader
import Material
import Program
import Shader
import Texture2D
import WebGL2RenderingContext
import colliders.SphereCollider
import move.ballCollideStrategy
import org.khronos.webgl.WebGLRenderingContext
import vision.gears.webglmath.Vec3
import kotlin.random.Random

class Ball(private val gl: WebGL2RenderingContext, private val size: Float) : GameObject() {
    private val vsTextured = Shader(gl, WebGLRenderingContext.VERTEX_SHADER, "textured-vs.glsl")
    private val fsTextured = Shader(gl, WebGLRenderingContext.FRAGMENT_SHADER, "textured-fs.glsl")
    private val texturedProgram = Program(gl, vsTextured, fsTextured)

    private val jsonLoader = JsonLoader()

    init {
        val mesh = jsonLoader.loadMeshes(gl,
            "media/sphere.json",
            Material(texturedProgram).apply{
                this["colorTexture"]?.set(
                    Texture2D(gl, "media/planets/earth_tiny.jpg")
                )
            }
        )[0]
        scale.set(size, size, size)
        position.set(Vec3(0f, size, 0f))

        addComponentsAndGatherUniforms(mesh)

        val velocity = Vec3(5f - Random.nextFloat() * 10f, 0f, 5f - Random.nextFloat() * 10f)
        move = ballCollideStrategy(velocity, gl)
        speed = velocity.length()
        collider = SphereCollider(this.position, size)
    }

}