package shop.retries

import derevo.cats.show
import derevo.derive

@derive(show)
sealed trait Retryable

object Retryable {
  case object Orders   extends Retryable
  case object Payments extends Retryable
}
