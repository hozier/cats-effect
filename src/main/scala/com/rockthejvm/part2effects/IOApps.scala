package com.rockthejvm.part2effects

import cats.effect.{ExitCode, IO, IOApp}
import scala.io.StdIn

object IOApps {
  val program = for {
    line <- IO(StdIn.readLine())
    _ <- IO(println(s"You've just written: $line"))
  } yield ()
}
