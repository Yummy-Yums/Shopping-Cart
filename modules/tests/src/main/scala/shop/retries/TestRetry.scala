package shop.retries

import scala.annotation.nowarn

import cats.effect.IO
import cats.effect.kernel.Ref
import retry.RetryDetails._
import retry._

object TestRetry {

  private[retries] def handleFor[A <: RetryDetails](
    ref: Ref[IO, Option[A]]
  ): Retry[IO] =
    new Retry[IO] {
      def retry[T](policy: RetryPolicy[IO], retryable: Retryable)(fa: IO[T]): IO[T] = {
        @nowarn
        def onError(e: Throwable, details: RetryDetails): IO[Unit] =
          details match {
            case a: A => ref.set(Some(a))
            case _    => IO.unit
          }

        retryingOnAllErrors[T](policy, onError)(fa)
      }
    }

  def givingUp(ref: Ref[IO, Option[GivingUp]]): Retry[IO] =
    handleFor[GivingUp](ref)

  def recovering(ref: Ref[IO, Option[WillDelayAndRetry]]): Retry[IO] =
    handleFor[WillDelayAndRetry](ref)

}
