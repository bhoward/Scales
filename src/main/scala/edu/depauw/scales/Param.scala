package edu.depauw.scales

class Param[T](valueFun: Time => T) {
  def v: TimeContext ?=> T = tc ?=> {
    valueFun(tc._1)
  }
}

object Param {
  def apply[T](value: T): Param[T] = new Param(_ => value)
  def apply[T](valueFun: Time => T): Param[T] = new Param(valueFun)
}

val foo = Param(42)
val bar = Param(t => 37)