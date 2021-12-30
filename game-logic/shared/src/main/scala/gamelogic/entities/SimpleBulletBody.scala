package gamelogic.entities
import gamelogic.entities.WithPosition.Angle
import be.doeraene.physics.Complex
import be.doeraene.physics.shape.{Circle, Shape}

/** A [[SimpleBulletBody]] goes forward at constant speed and damage the first thing it its.
  * @param range
  *   maximal distance this bullet can travel
  * @param ownerId
  *   the entity id of the entity responsible for creating this bullet.
  */
final case class SimpleBulletBody(
    id: Entity.Id,
    time: Long,
    pos: Complex,
    speed: Double,
    radius: Double,
    direction: Double,
    range: Double,
    ownerId: Entity.Id
) extends MovingBody {
  val shape: Shape     = new Circle(radius)
  val rotation: Double = 0.0 // doesn't matter, it's a disk
  val moving: Boolean  = true

  def move(
      time: Long,
      position: Complex,
      direction: Angle,
      rotation: Angle,
      speed: Double,
      moving: Boolean
  ): MovingBody =
    copy(time = time, pos = position, direction = direction, speed = speed)

  // Let's make it neutral, it was only intended to test.
  def teamId: Entity.TeamId = Entity.teams.neutralTeam
}

object SimpleBulletBody {
  final def defaultRadius: Double = 4
}
