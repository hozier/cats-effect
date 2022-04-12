package com.rockthejvm.part2effects

import scala.concurrent.Future
import java.util.Calendar
import com.rockthejvm.part2effects.Effects.Excercise

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

    /*
      sample usage:
        val sampleCallback = () => {
          println("Running usage sample ...")
          42
        }
        val sampleCall: IO[Int] = IO(sampleCallback)
     */
  }
  object Excercise {

    /** @note
      *   for each solution object, implement the description of function.
      *
      * @return
      *   an IO with its `description` -- a callback argument: () => A
      */
    object CurrSysTime {
      def description[F[A] >: IO[A], A]: A => F[A] = curr => {
        println(s"curr=$curr")
        IO(() => curr)
      }
      def main: Unit = {
        description( /* UTC timestamp in seconds */
          System.currentTimeMillis / 1000
        )
        description(
          s"${System.currentTimeMillis}" /* Unix timestamp in milliseconds */
        )
        description(
          Calendar.getInstance /* Time via Calendar object meta */
        )
      }
    }
    object DurationMeasurement {
      def description[F[A] >: IO[A], A]: (A) => F[A] = delta => {
        println(s"duration=$delta")
        IO(() => delta)
      }
      def computation: IO[Unit] = IO(() => Thread.sleep(500))
      def measure: () => IO[Long] = () => {
        for {
          start <- CurrSysTime.description(System.currentTimeMillis)
          _ <- computation
          end <- CurrSysTime.description(System.currentTimeMillis)
          _ <- description(end - start) /* so. so. cool! */
        } yield end - start
      }
    }
    object Print {
      def description[F[A] >: IO[A], A]: (A) => F[Unit] = data => {
        IO(() => println(s"data=$data"))
      }
    }
  }
}
def main(args: Array[String]): Unit = {
  Excercise.DurationMeasurement.measure().unsafeRun()
  Excercise.Print.description("print some data").unsafeRun()
}
