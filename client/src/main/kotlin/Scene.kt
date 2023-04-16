import gameObjects.car.Ball
import gameObjects.car.Car
import org.w3c.dom.HTMLCanvasElement
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec3
import kotlin.js.Date
import kotlin.math.PI
import kotlin.random.Random
import org.khronos.webgl.WebGLRenderingContext as GL

class Scene (
  val gl : WebGL2RenderingContext)  : UniformProvider("scene") {

  val vsTextured = Shader(gl, GL.VERTEX_SHADER, "textured-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
  val texturedProgram = Program(gl, vsTextured, fsTextured)

  val vsEnvMap = Shader(gl, GL.VERTEX_SHADER, "env-vs.glsl")
  val fsEnvMap = Shader(gl, GL.FRAGMENT_SHADER, "env-fs.glsl")
  val envMapProgram = Program(gl, vsEnvMap, fsEnvMap)

  val texturedQuadGeometry = TexturedQuadGeometry(gl)

  val gameObjects = ArrayList<GameObject>()

  val camera = PerspectiveCamera(*Program.all).apply{
    position.set(0f, 30f, -40f)
    lookAt(Vec3(0f, 0f, 0f))
    //windowSize.set(20f, 20f)
    //updateViewProjMatrix()
  }

  fun resize(canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    camera.setAspectRatio(canvas.width.toFloat()/canvas.height)
  }

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  val envTexture = TextureCube(gl,
    "media/posx512.jpg",
    "media/negx512.jpg",
    "media/posy512.jpg",
    "media/negy512.jpg",
    "media/posz512.jpg",
    "media/negz512.jpg"
  )

  val backgroundMaterial = Material(envMapProgram).apply{
    this["envTexture"]?.set(envTexture)
  }

  val envMesh = Mesh(backgroundMaterial, texturedQuadGeometry)
  val envGameObject = GameObject(envMesh)

  val car = Car(gl)

  val groundMaterial = Material(texturedProgram).apply{
    this["colorTexture"]?.set(Texture2D(gl, "media/ground.jpg"))
  }
  val groundGeometry = TexturedQuadGeometry(gl, 100, 100)
  val groundMesh = Mesh(groundMaterial, groundGeometry)
  val groundGameObject = GameObject(groundMesh).apply{
    position.set(0f, 0f, 0f)
    pitch = -PI.toFloat() / 2.0f
    scale.set(2000f, 2000f, 1f)
  }

  val balls = ArrayList<Ball>()

  init{
    gl.enable(GL.DEPTH_TEST)
    addComponentsAndGatherUniforms(*Program.all)
    gameObjects += groundGameObject
    gameObjects += car.getGameObjects()

    for(i in 0..200){
      val ball = Ball(gl, 4f)
      ball.position.set(200 - Random.nextFloat() * 400, ball.position.y, 200 - Random.nextFloat() * 400)
      balls += ball
      gameObjects += ball
    }
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>, ) {
    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame

    val cameraAnchor = car.backward.clone() * 50.0f
    cameraAnchor.y = 20.0f

    camera.followObject(car.getPosition() + cameraAnchor, 30.0f)
    camera.lookAt(car.getPosition())

    gl.useProgram(texturedProgram.glProgram)
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    gl.enable(GL.BLEND)
    gl.blendFunc(
      GL.SRC_ALPHA,
      GL.ONE_MINUS_SRC_ALPHA)

    gameObjects.forEach{ it.move(dt, t, keysPressed, gameObjects) }

    gameObjects.forEach{ it.update() }
    gameObjects.forEach{ it.draw(this, camera) }

    gl.useProgram(envMapProgram.glProgram)

    envGameObject.draw(camera)
  }
}
