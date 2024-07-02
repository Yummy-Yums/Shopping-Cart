package shop.domain

import java.util.UUID

import shop.domain.brand.BrandId
import shop.domain.category.CategoryId
import shop.domain.healthcheck.Status
import shop.generators._
import shop.optics.IsUUID

import monocle.law.discipline._
import org.scalacheck.{ Arbitrary, Cogen, Gen }
import weaver.FunSuite
import weaver.discipline.Discipline

object OpticsSuite extends FunSuite with Discipline {

  implicit val arbStatus: Arbitrary[Status] =
    Arbitrary(Gen.oneOf(Status.Okay, Status.Unreachable))

  implicit val brandIdArb: Arbitrary[BrandId] =
    Arbitrary(brandIdGen)

  implicit val brandIdCogen: Cogen[BrandId] =
    Cogen[UUID].contramap[BrandId](_.value)

  checkAll("Iso[Status._Bool]", IsoTests(Status._Bool))

  // bonus checks
  checkAll("IsUUID[UUID]", IsoTests(IsUUID[UUID]._UUID))
  checkAll("IsUUID[BrandId]", IsoTests(IsUUID[BrandId]._UUID))

}
