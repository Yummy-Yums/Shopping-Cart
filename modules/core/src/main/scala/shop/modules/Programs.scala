package shop.modules

import cats.effect.Temporal
import cats.implicits.catsSyntaxSemigroup
import org.typelevel.log4cats.Logger
import retry.RetryPolicies.{ exponentialBackoff, limitRetries }
import retry.RetryPolicy
import shop.config.types._
import shop.effects._
import shop.programs._

object Programs {

  def make[F[_]: Background: Logger: Temporal](
      checkoutConfig: CheckoutConfig,
      services: Services[F],
      clients: HttpClients[F]
  ): Programs[F] =
    new Programs[F](checkoutConfig, services, clients) {}
}

sealed abstract class Programs[
    F[_]: Background: Logger: Temporal
] private (
    cfg: CheckoutConfig,
    services: Services[F],
    clients: HttpClients[F]
) {

  val retryPolicy: RetryPolicy[F] =
    limitRetries[F](cfg.retriesLimit.value) |+|
      exponentialBackoff[F](cfg.retriesBackoff)

  val checkout: Checkout[F] = Checkout[F](
    clients.payment,
    services.cart,
    services.orders,
    retryPolicy
  )
}
