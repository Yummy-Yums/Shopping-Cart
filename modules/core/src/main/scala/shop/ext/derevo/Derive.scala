package shop.ext.derevo

import scala.annotation.implicitNotFound

import derevo.{ Derivation, NewTypeDerivation }

trait Derive[F[_]] extends Derivation[F] with NewTypeDerivation[F] {
  def instance(implicit ev: OnlyNewTypes): Nothing = ev.absurd

  @implicitNotFound("Only newtypes instances can be derived")
  abstract final class OnlyNewTypes {
    def absurd: Nothing
  }

}
