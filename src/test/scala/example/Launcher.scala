// See LICENSE.txt for license details.
package deepnet

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}
import utils.TutorialRunner

object Launcher {
  val examples = Map(
      "Arith" -> { (backendName: String) =>
        Driver(() => new Arith(), backendName) {
          (c) => new ArithUnitTest(c)
        }
      },

     "ALU" -> { (backendName: String) =>
        Driver(() => new ALU(), backendName) {
        (c) => new ALUUnitTester(c)
        }
      },

      "GCD" -> { (backendName: String) =>
        Driver(() => new GCD(), backendName) {
        (c) => new GCDUnitTester(c)
        }
      }
  )
  def main(args: Array[String]): Unit = {
    TutorialRunner(examples, args)
  }
}

