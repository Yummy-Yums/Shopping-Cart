package shop.domain

import derevo.cats.show
import derevo.circe.magnolia.encoder
import derevo.derive
import shop.domain.auth.UserId
import shop.domain.checkout.Card
import squants.market.Money

object payment {

  @derive(encoder, show)
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )

}
