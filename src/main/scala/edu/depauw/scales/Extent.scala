import scala.math.Numeric.Implicits.infixNumericOps
import scala.math.Ordering.Implicits.infixOrderingOps

// Constraint: lo <= 0 <= hi
case class Extent[T: Numeric](lo: T, hi: T) {
  def len: T = hi - lo

  def par(that: Extent[T]): Extent[T] = {
    Extent(this.lo min that.lo, this.hi max that.hi)
  }

  def seqLo(that: Extent[T]): Extent[T] = {
    // Shift that earlier by that.hi, so that.hi aligns with this origin
    Extent(this.lo min (that.lo - that.hi), this.hi)
  }

  def seqHi(that: Extent[T]): Extent[T] = {
    // Shift that later by this.hi, so that origin aligns with this.hi
    Extent(this.lo min (that.lo + this.hi), this.hi + that.hi)
  }

  // TODO also have shifts so this.hi aligns with that.lo, etc.?
}
