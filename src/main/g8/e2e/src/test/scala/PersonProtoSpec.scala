package mygen

import org.scalatest._
import mygen.protos.demo.PersonBoo

class PersonProtoSpec extends FlatSpec with MustMatchers {
  "PersonBoo" should "have correct field count" in {
    PersonBoo.FieldCount must be(2)
  }
}
