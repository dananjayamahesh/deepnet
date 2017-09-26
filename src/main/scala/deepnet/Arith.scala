// See LICENSE for license details.

package deepnet

import chisel3._
import chisel3.util._

class Arith extends Module{
  val io = IO(new Bundle {
    val en = Input (Bool())
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val sel = Input(UInt(4.W))
    val out = Output(UInt(32.W))
  })

  val out_reg = Reg(UInt(32.W))
  when (io.en){
     switch(io.sel){
       is(1.U) {out_reg := io.a + io.b}
       is(2.U) {out_reg := io.a - io.b}
       is(3.U) {out_reg := io.a / io.b}
       is(4.U) {out_reg := io.a * io.b}
     }
  }
  io.out := out_reg

}
