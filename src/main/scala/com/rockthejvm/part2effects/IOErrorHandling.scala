package com.rockthejvm.part2effects

import cats.effect.IO
import scala.util.{Failure, Success, Try}

/** @note
  *   Covers: cats.effect.IO - creating `failed` computations suspended in IO
  */
object IOErrorHandling {
  def effectAsEither: IO[Int] => IO[Either[Throwable, Int]] = n => n.attempt
  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global
    effectAsEither(IO(4)).flatMap(either =>
      either match {
        case Left(e)  => IO { println(s"rejected=$e") }
        case Right(v) => IO { println(s"resolved=$v") }
      }
    )
  }
}
