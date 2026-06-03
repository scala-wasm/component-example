package example

import org.junit.Assert.assertEquals
import org.junit.Test

class SmokeTest {
  @Test
  def mock(): Unit =
    assertEquals(1, 1)
}
