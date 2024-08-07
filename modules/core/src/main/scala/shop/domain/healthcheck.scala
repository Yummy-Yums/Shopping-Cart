package shop.domain

import derevo.cats.eqv
import derevo.circe.magnolia.encoder
import derevo.derive
import io.estatico.newtype.macros._
import io.circe.Encoder
import monocle.Iso

object healthcheck {

  trait HealthCheck[F[_]] {
    def status: F[AppStatus]
  }

  @derive(eqv)
  sealed trait Status
  object Status {
    case object Okay        extends Status
    case object Unreachable extends Status

    val _Bool: Iso[Status, Boolean] =
      Iso[Status, Boolean] {
        case Okay        => true
        case Unreachable => false
      }(if (_) Okay else Unreachable)

    implicit val jsonEncoder: Encoder[Status] =
      Encoder.forProduct1("status")(_.toString)

  }

  @derive(encoder)
  @newtype
  case class RedisStatus(value: Status)

  @derive(encoder)
  @newtype
  case class PostgresStatus(value: Status)

  @derive(encoder)
  case class AppStatus(
      redis: RedisStatus,
      postgres: PostgresStatus
  )

}
