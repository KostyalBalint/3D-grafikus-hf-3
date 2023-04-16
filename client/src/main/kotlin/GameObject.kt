import colliders.Collider
import colliders.NullCollider
import vision.gears.webglmath.*

open class GameObject(
  vararg val meshes : Mesh
   ) : UniformProvider("gameObject") {

  val position = Vec3()
  var roll = 0.0f
  var pitch = 0.0f
  var yaw = 0.0f
  val scale = Vec3(1.0f, 1.0f, 1.0f)

  var collider : Collider = NullCollider()

  var speed = 0.0f

  val modelMatrix by Mat4()
    val modelMatrixInv by Mat4()
    var inverseMass = 0.0f

  var parent : GameObject? = null

  init {
    addComponentsAndGatherUniforms(*meshes)
  }

  fun update() {
    modelMatrix.set().
      scale(scale).
      rotate(roll, Vec3(0.0f, 0.0f, 1.0f)).
        rotate(pitch, Vec3(1.0f, 0.0f, 0.0f)).
        rotate(yaw, Vec3(0.0f, 1.0f, 0.0f)).
      translate(position)
    parent?.let{ parent -> 
      modelMatrix *= parent.modelMatrix
    }
    modelMatrixInv.set(modelMatrix.clone().invert())
  }

  open class Motion(val gameObject : GameObject) {
    open operator fun invoke(
        dt : Float = 0.016666f,
        t : Float = 0.0f,
        keysPressed : Set<String> = emptySet<String>(),
        gameObjects : List<GameObject> = emptyList<GameObject>()
        ) : Boolean {
      return true;
    }
  }
  var move = Motion(this)

}
