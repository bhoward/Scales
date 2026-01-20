type Time = Long // Double?

trait Event {
  def duration: Time

  def before(that: Event): Event = {
    CompoundEvent(this.duration + that.duration, this, that.delay(this.duration))
  }

  def after(that: Event): Event = {
    CompoundEvent(this.duration, this, that.delay(-that.duration))
  }

  def during(that: Event): Event = {
    CompoundEvent(this.duration max that.duration, this, that)
  }

  def delay(time: Time): Event = {
    DelayEvent(this, time)
  }

  def render(start: Time)(using context: Context): Seq[(Time, PrimEvent)]
}

trait PrimEvent extends Event {
  def render(start: Time)(using context: Context): Seq[(Time, PrimEvent)] = Seq(start -> this)
}

type TC = (Time, Context)

class DemoEvent(val name: TC ?=> String) extends PrimEvent {
  val duration = 1

  override def render(start: Time)(using context: Context): Seq[(Long, PrimEvent)] = {
    given TC = (start, context)
    Seq(start -> ResolvedDemoEvent(name + context.demo)) // evaluate name here, with context
  }
}

object DemoEvent {
  def apply(name: TC ?=> String): DemoEvent = new DemoEvent(name)
}

case class ResolvedDemoEvent(name: String) extends PrimEvent {
  val duration = 1

  override def toString: String = name
}

case class CompoundEvent(duration: Time, events: Event*) extends Event {
  override def before(that: Event): Event = {
    CompoundEvent(this.duration + that.duration, this.events :+ that.delay(this.duration)*)
  }

  override def after(that: Event): Event = {
    CompoundEvent(this.duration, this.events :+ that.delay(-that.duration)*)
  }

  override def during(that: Event): Event = {
    CompoundEvent(this.duration max that.duration, this.events :+ that*)
  }

  def render(start: Time)(using context: Context): Seq[(Time, PrimEvent)] = {
    events.flatMap(_.render(start))
  }
}

case class DelayEvent(event: Event, time: Time) extends Event {
  override def duration: Time = event.duration

  override def before(that: Event): Event = {
    DelayEvent(this.event.before(that), time)
  }

  override def after(that: Event): Event = {
    DelayEvent(this.event.after(that), time)
  }

  override def during(that: Event): Event = {
    DelayEvent(this.event.during(that), time)
  }

  override def delay(time: Time): Event = {
    DelayEvent(this.event, this.time + time)
  }

  def render(start: Time)(using context: Context): Seq[(Time, PrimEvent)] = {
    this.event.render(start + time)
  }
}

@main def demo(): Unit = {
  given global: Context = new Context() // TODO

  val p = Param((t: Time) => if t == 0 then "B0" else "B1")

  val a = DemoEvent("A")
  val b = DemoEvent(p.v)
  val c = DemoEvent((tc: TC) ?=> tc._1.toString)

  println(a.render(0))
  println(a.before(b).render(0))

  val x = a.after(b)
  val y = x.before(c)
  println(x.render(0))
  println(y.render(0))

  println(c.before(x).during(y).before(DemoEvent("D").before(DemoEvent("E"))).render(0))
}
