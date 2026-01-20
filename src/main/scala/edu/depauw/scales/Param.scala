case class Param[T](valueFun: Time => T) {
  def v: TC ?=> T = (tc: TC) ?=> {
    valueFun(tc._1)
  }
}

object Param {
  def apply[T](value: T): Param[T] = Param(_ => value)
}

val foo = Param(42)
val bar = Param((t: Time) => 37)