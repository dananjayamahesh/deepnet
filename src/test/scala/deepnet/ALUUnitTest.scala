// See LICENSE for license details.

package deepnet

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import deepnet.ALU

class ALUUnitTester(c: ALU) extends PeekPokeTester(c) {
  /**
    * compute the gcd and the number of steps it should take to do it
    *
    * @param a positive integer
    * @param b positive integer
    * @return the GCD of a and b
    */
  def computeGcd(a: Int, b: Int): (Int, Int) = {
    var x = a
    var y = b
    var depth = 1
    while(y > 0 ) {
      if (x > y) {
        x -= y
      }
      else {
        y -= x
      }
      depth += 1
    }
    (x, depth)
  }

  private val gcd = c

  for(i <- 1 to 40 by 3) {
    for (j <- 1 to 40 by 7) {

      poke(gcd.io.a, i)
      poke(gcd.io.b, j)
      poke(gcd.io.e, 1)
      step(1)
      poke(gcd.io.e, 0)

      val (expected_gcd, steps) = computeGcd(i, j)

      step(steps - 1) // -1 is because we step(1) already to toggle the enable
      expect(gcd.io.z, expected_gcd)
      expect(gcd.io.v, 1)
    }
  }
}

class ALUTester extends ChiselFlatSpec {
  private val backendNames = Array[String]("firrtl", "verilator")
  for ( backendName <- backendNames ) {
    "ALU" should s"calculate proper greatest common denominator (with $backendName)" in {
      Driver(() => new ALU, backendName) {
        c => new ALUUnitTester(c)
      } should be (true)
    }
  }
}
