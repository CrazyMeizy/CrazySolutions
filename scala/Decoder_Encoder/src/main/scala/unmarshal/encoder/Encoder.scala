package unmarshal.encoder

import cats.implicits.catsSyntaxSemigroup
import shapeless._
import shapeless.HList
import shapeless.labelled.FieldType
import unmarshal.model.Json
import unmarshal.model.Json._

trait Encoder[A] {
  def toJson(value: A): Json
}

object Encoder {

  def apply[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

  def autoDerive[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

  implicit def autoDeriveEncoder[A, H <: HList](implicit
    labelledGeneric: LabelledGeneric.Aux[A, H],
    hEncoder: Lazy[ObjectEncoder[H]]
  ): Encoder[A] = new Encoder[A] {
    def toJson(value: A): Json = hEncoder.value.encode(labelledGeneric.to(value))
  }

  trait ObjectEncoder[H <: HList] {
    def encode(value: H): JsonObject
  }

  object ObjectEncoder {

    implicit val hnilObjectEncoder: ObjectEncoder[HNil] = (_: HNil) => JsonObject(Map.empty)

    implicit def hconsObjectEncoder[K <: Symbol, V, T <: HList](implicit
      witness: Witness.Aux[K],
      hEncoder: Lazy[Encoder[V]],
      tEncoder: ObjectEncoder[T]
    ): ObjectEncoder[FieldType[K, V] :: T] = (value: FieldType[K, V] :: T) => {
      val fieldName = witness.value.name
      val headJson  = hEncoder.value.toJson(value.head)
      val headObj   = JsonObject(Map(fieldName -> headJson))
      val tailObj   = tEncoder.encode(value.tail)
      headObj |+| tailObj
    }

  }

  implicit val stringEncoder: Encoder[String] = (value: String) => JsonString(value)

  implicit val intEncoder: Encoder[Int] = (value: Int) => JsonNum(value.toLong)

  implicit val longEncoder: Encoder[Long] = (value: Long) => JsonNum(value)

  implicit val doubleEncoder: Encoder[Double] = (value: Double) => JsonDouble(value)

  implicit val booleanEncoder: Encoder[Boolean] = (value: Boolean) =>
    if (value) JsonBool(true) else JsonBool(false)

  implicit def optionEncoder[V](implicit
    encoder: Encoder[V]
  ): Encoder[Option[V]] = {
    case Some(v) => encoder.toJson(v)
    case None    => JsonNull
  }

  implicit def listEncoder[V](implicit
    encoder: Encoder[V]
  ): Encoder[List[V]] = new Encoder[List[V]] {
    def toJson(value: List[V]): Json = JsonArray(value.map(encoder.toJson))
  }

}
