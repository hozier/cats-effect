package com.rockthejvm.part2effects

import cats.effect.IO
import scala.io.StdIn

/** Covers: cats.effect.IO - embodies any kind of computation that might perform
  * side effects
  */
object IOIntroduction {
  object Excercise {

    /** sequence two IOs, take the result of the last one. */
    def seqTakeLast[A, B]: (IO[A], IO[B]) => IO[B] = (a, b) => {
      a.flatMap(_ => b)
    }

    /** sequence two IOs, take the result of the first one. */
    def seqTakeFirst[A, B]: (IO[A], IO[B]) => IO[A] = (a, b) => {
      a.flatMap(v => b.map(_ => v))
    }

    /** repeat an IO forever (called by name/delayed) */
    def forever[A]: IO[A] => IO[A] = a => a >> forever(a)

    /** convert an IO to a different type */
    def convert[A, B]: (IO[A], B) => IO[B] = (a, v) => { a.flatMap(_ => IO(v)) }

    /** discard value inside an IO */
    def discard[A]: IO[A] => IO[Unit] = a => a.map(_ => ())

    /** recursion */
    def sum: (IO[Int], Int) => IO[Int] = (n, acc) => {
      n.flatMap(number =>
        number match {
          case k if k > 0 => sum(IO(k - 1), acc + k)
          case 0          => IO(acc)
        }
      )
    }
  }
  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global
    val solution = for {
      total <- Excercise.sum(IO(20000), 0)
      // _ <- Excercise.forever(IO {
      //   println(s"sleeping=400ms")
      //   Thread.sleep(400)
      //   println(s"awake")
      //   27
      // })
      _ <- IO(println(s"total=$total"))
    } yield ()

    solution.unsafeRunSync()
  }
}
