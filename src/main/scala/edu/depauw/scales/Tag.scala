package edu.depauw.scales

class Tag[T](default: T = null) {
  def apply(): TimeContext ?=> T = tc ?=> {
    val context = tc._2
    context.getOrElse(this, default)
  }
}
