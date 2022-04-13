package com.rockthejvm.part2effects

import cats.Parallel
import cats.effect.{IO, IOApp}
import scala.util.Random
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.rockthejvm.utils._

/** Covers: cats.effect.IO - traverse is a concept that allows double nested
  * structured to be defined `inside-out` (for ease of processing later).
  * traversing becomes powerful when we also add the parallel concept of an IO
  * data type. We can distribute workload in our parallel application very, very
  * easily with a combination of parallel and traverse.
  */
object IOTraversal extends IOApp.Simple {
  import cats.instances.list._
  import cats.{Traverse}
  import cats.syntax.parallel._
  object Operations {
    object mock {
      val workLoad: List[String] =
        List(
          "Hey there Delilah",
          "How's it like in New York City",
          "Oh yes, it's true"
        )
      val async: String => Int = str =>
        str match {
          case s: String =>
            Thread.sleep(Random.nextInt(1000))
            s.split(" ").length
        }
    }
    val heavyComputation: String => Future[Int] = str =>
      Future { mock.async(str) }
    val computeAsIO: String => IO[Int] = str => IO { mock.async(str) }
  }
  val listTraverse = Traverse[List]
  def traverseFutures: Future[List[Int]] = {
    val clunkyFutures: List[Future[Int]] = Operations.mock.workLoad.map {
      Operations.heavyComputation
    }
    /* this store ALL the results as a `flattened` List[Int]*/
    val singleFuture = listTraverse.traverse(Operations.mock.workLoad)(
      Operations.heavyComputation
    )
    singleFuture
  }

  /** call traverse on an IOs example */
  def traverseIOs: IO[List[Int]] = {

    /** without traversing: */
    val clunkyIOs: List[IO[Int]] =
      Operations.mock.workLoad.map { Operations.computeAsIO(_).debug }

    /** with traversing: */
    val singleIO: IO[List[Int]] = /* traverse is similar to workLoad.map() */
      listTraverse.traverse(Operations.mock.workLoad) {
        Operations.computeAsIO(_).debug
      }

    /** with traversing in parallel: */
    val parallelizedSingleIO: IO[List[Int]] =
      Operations.mock.workLoad.parTraverse { Operations.computeAsIO(_).debug }

    parallelizedSingleIO
  }
  object Excerise {
    def sequenceGeneralized[F[_]: Traverse, A]: F[IO[A]] => IO[F[A]] =
      listOfIOs => {
        /* leverage the sequence type class */
        val listTraverse = Traverse[F]
        listTraverse.traverse(listOfIOs)(identity)
      }
  }
  override def run: IO[Unit] = {
    // IO { traverseFutures }.debug.void
    traverseIOs.void
  }
}
