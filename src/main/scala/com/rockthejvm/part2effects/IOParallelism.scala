package com.rockthejvm.part2effects

import cats.Parallel
import cats.effect.{IO, IOApp}

/** @note
  *   Covers: cats.effect.IO - threading on IOs. cool things to note:
  *
  *   - leverage Cats' IO.Par type for parallel operations
  *   - the failure of the entire effect (within a parallel IO) is the first one
  *     to fail
  */
object IOParallelism extends IOApp.Simple {

  /** extension methods provide the ability to attach functionality directly
    * unto a desired type
    */
  extension [A](io: IO[A])
    def debug: IO[A] = for {
      a <- io
      t = Thread.currentThread().getName
      _ = println(s"[${t}] $a")
    } yield a

  override def run: IO[Unit] = IO {}
}
