package shop.http.routes.secured

import cats.MonadThrow
import cats.syntax.all._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{ AuthMiddleware, Router }
import shop.domain.cart.CartNotFound
import shop.domain.checkout.Card
import shop.domain.order.{ EmptyCartError, OrderOrPaymentError }
import shop.ext.http4s.refined._
import shop.http.auth.users.CommonUser
import shop.programs.Checkout

final case class CheckoutRoutes[F[_]: JsonDecoder: MonadThrow](
    checkout: Checkout[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/checkout"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {

    case ar @ POST -> Root as user =>
      ar.req.decodeR[Card] { card =>
        checkout
          .process(user.value.id, card)
          .flatMap(Created(_))
          .recoverWith {
            case CartNotFound(userId) =>
              NotFound(
                s"Cart not found user: ${userId.value}"
              )
            case EmptyCartError =>
              BadRequest("Shopping cart is empty!")

            case e: OrderOrPaymentError =>
              BadRequest(e.show)
          }
      }
  }

  def routes(
      authMiddleware: AuthMiddleware[F, CommonUser]
  ): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}
