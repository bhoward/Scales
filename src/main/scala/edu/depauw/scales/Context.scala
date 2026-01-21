package edu.depauw.scales

class Context {
  import scala.collection.mutable.Map

  val map = Map.empty[Any, Any]

  def demo: String = "Boo!" // just for demo

  def put[T](tag: Tag[T], value: T): Unit = {
    map.put(tag, value)
  }

  def getOrElse[T](tag: Tag[T], default: T): T = {
    map.getOrElse(tag, default).asInstanceOf[T]
  }
}
