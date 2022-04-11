package com.rockthejvm.part1recap

/** @note
  *   Covers: 1 - given instances, 2 - extension methods, 3 - combining
  *   given/using to implement type classes
  */
object ContextualAbstractionsScala3 {
  def main(args: Array[String]): Unit = {

    /** @note
      *   1 - given instances are equiv to implicit args/value from scala 2
      */
    def increment: (Int => (Int => Int)) = x => (amount) => x + amount
    val twelve = increment(10)(2)

    /** given/using pairs allow us to anchor an argument value to a function
      * when `given` is applied, the value declared is automatically passed by
      * the compiler to the `using` variable
      */
    given defaultAmount: Int = 10
    def incrementWithUsing(x: Int)(using amount: Int): Int = x + amount
    val thirteen = incrementWithUsing(3)
    println(s"thirteen=$thirteen")
  }
}
