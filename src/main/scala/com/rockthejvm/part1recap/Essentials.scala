package com.rockthejvm.part1recap

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

object Essentials {

  // immutable val, expression review, etc.
  def main(args: Array[String]): Unit = {

    /** @note
      *   Futures review Furtures are data structures whose values are computed
      *   on some other thread at some point in the future. Futures will need an
      *   implicit execution context (a data structure that will hold the
      *   mechanism for scheduling threads).
      */

    /** @note
      *   the typical convention for working with ecs would normally be to:
      *   import scala.concurrent.ExecutionContext.Implicits.global however we
      *   will explicitly leverage our own
      */

    /** ec: explicitly spawns exection context. starts a thread pools which
      * serves as a plaform for the exection context.
      */
    implicit val ec: ExecutionContext =
      ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

    /** this value of the Future block will be run on another thread */
    val future = Future { 42 }

    /** @note
      *   in order to operate on this future above, we can use the oncomplete
      *   function. the onComplete recieves a Try object. and as such we can
      *   send in a partial function to pattern match various cases
      *
      * waits for completion
      */
    future.onComplete {
      case Success(value) => println(s"The async meaning of life is $value")
      case Failure(exception) =>
        println(s"Meaning of value failed is $exception")
    }

    /** an alternative way of operating on Futures can be seen below */
    val impovedFuture = future.map(_ + 1)
  }
}
