package com.rockthejvm.part2effects

import scala.concurrent.Future

/** @note
  *   FP: implemented `descriptions` of computation
  * @example
  *   Concretely: an `effect` is a `data type` that embodies the concept of a) a
  *   side effect or b) any sort of computation that we might need in our code.
  *   Abstractly: the concept of a purely functional program may be defined as a
  *   giant expression that can very easily be replaced w/ the value it
  *   evaluates to (referential transparency). eg (expr --> value) == (value -->
  *   expr)
  */
object Effects {

  /** a) custom IO `describes` the value that will be calculated inside.
    *
    * b) the data structure `creation` is seperate from
    *
    * c) the performance of the computation.
    */
  case class IO[A](unsafeRun: () => A) {
    def map[B](f: A => B): IO[B] = {
      IO(() => f(unsafeRun()))
    }
    def flatMap[B](f: A => IO[B]): IO[B] = {
      IO(() => f(unsafeRun()).unsafeRun())
    }
  }
  def main(args: Array[String]): Unit = {}
}
