package com.rockthejvm.part3concurrency

import cats.effect.kernel.Outcome.{Canceled, Errored, Succeeded}
import cats.effect.{Fiber, IO, FiberIO, IOApp, Outcome}
import scala.concurrent.duration._

/** Covers: cats.effect.Fiber - can be described as one of the primitives of
  * Cats Effect Concurrency. Fiber is the desription of a computation that will
  * run on some thread scheduled or managed by the Cats Effect runtime.
  */
object Fibers extends IOApp.Simple {
  import com.rockthejvm.utils._
  object Overview {
    val meaningOfLife = IO.pure(42)
    val favLang = IO.pure("Scala")

    /** again, we can compose these IOs with for comprehensions. remember, for
      * comprehensions expand as chains of map and flatMap, then it must be that
      * when `pure` effects are used, they are actually `run sequentially` on
      * the same thread.
      */
    def sameThreadIOs() = for {
      life <- meaningOfLife.debug
      fav <- favLang.debug
    } yield ()

    /** Introduce: the Fiber (created through the c-e api) . To perform an
      * effect on some other thread, simply call `start`.
      */
    val theAwesomeFiber = meaningOfLife.debug.start
    def differentThreadIOs() = for {
      _ <- theAwesomeFiber
      _ <- favLang.debug
    } yield ()

    /** Manage: the Fiber lifecycle (join). We wait on a Fiber to finish in a
      * purely functional way when we invoke join. The possible outcomes of a
      * `join ` call are:
      *
      * a) Success with an IO
      *
      * b) Failure with an Exception
      *
      * c) Cancelled
      */
    def runOnSomeOtherThread[A](io: IO[A]) = for {
      fib <- io.start /* effect being performed on a different thread */
      result <-
        fib.join /* effect, when performed, waits on the Fiber to terminate. */
    } yield result
  }
  override def run =
    Overview
      .runOnSomeOtherThread(Overview.meaningOfLife)
      .debug
      .void // Succeeded(IO(42)) <- [wrapped in an IO]
}
