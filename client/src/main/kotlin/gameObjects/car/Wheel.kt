package gameObjects.car

import GameObject
import JsonLoader
import Material
import Program
import Shader
import Texture2D
import WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext

class Wheel(private val gl: WebGL2RenderingContext) : GameObject() {
    private val vsTextured = Shader(gl, WebGLRenderingContext.VERTEX_SHADER, "textured-vs.glsl")
    private val fsTextured = Shader(gl, WebGLRenderingContext.FRAGMENT_SHADER, "textured-fs.glsl")
    private val texturedProgram = Program(gl, vsTextured, fsTextured)

    private val jsonLoader = JsonLoader()


    init {
        val chevyWheel = jsonLoader.loadMeshes(gl,
            "media/json/chevy/wheel.json",
            Material(texturedProgram).apply{
                this["colorTexture"]?.set(
                    Texture2D(gl, "media/json/chevy/chevy.png")
                )
            }
        )[0]
        addComponentsAndGatherUniforms(chevyWheel)
    }
}