// See LICENSE for license details.

package deepnet

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import deepnet.Arith

class ArithUnitTest(c: Arith) extends PeekPokeTester(c) {
  /**
    * compute the gcd and the number of steps it should take to do it
    *
    * @param a positive integer
    * @param b positive integer
    * @return the GCD of a and b
    */
  def computeArith(a: Int, b: Int): (Int, Int) = {
    var x = a
    var y = b
    var depth = 1

      depth += 1

    (x+y, depth)
  }

  private val arith = c

  for(i <- 1 to 100 by 1) {
    for (j <- 1 to 100 by 1) {

      poke(arith.io.a, i)
      poke(arith.io.b, j)
      poke(arith.io.sel,1)
      poke(arith.io.en, 1)
      step(1)
      poke(arith.io.en, 0)

      val (expected_arith, steps) = computeArith(i, j)

      step(steps - 1) // -1 is because we step(1) already to toggle the enable
      expect(arith.io.out, expected_arith)
      //expect(gcd.io.v, 1)
    }
  }
}

class ArithTester extends ChiselFlatSpec {
  private val backendNames = Array[String]("firrtl", "verilator")
  for ( backendName <- backendNames ) {
    "Arith" should s"calculate proper greatest common denominator (with $backendName)" in {
      Driver(() => new Arith, backendName) {
        c => new ArithUnitTest(c)
      } should be (true)
    }
  }
}
