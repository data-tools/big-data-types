package org.datatools.bigdatatypes.types.basic

import org.datatools.bigdatatypes.UnitSpec

class SqlTypeSpec extends UnitSpec {

  behavior of "SqlTypeSpec"

  "Nullable" should "not change to Required" in {
    SqlInt(Nullable).changeMode(Required) shouldBe SqlInt(Nullable)
  }

  "Required" should "change to Nullable" in {
    SqlInt(Required).changeMode(Nullable) shouldBe SqlInt(Nullable)
  }

}
