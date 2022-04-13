package com.rockthejvm.part1recap

/** Covers: Fundamental Cats type classes needed to develop with cats-effect.
  * Cats is a functional programming library based on type classes. Cats has
  * various type classes which include:
  *
  * @note
  *   a) functor b) applicative c) flatMap d) monad e) apply f)
  *   applicativeError/mondaError g) traverse
  */
object CatsTypeClasses {

  /** a) functor
    * @note
    *   describes a data type that is mappable, eg collections/containers. we
    *   can view the below higher kinded as an untyped `container` described by
    *   `F`. functors are used to define very generalisable apis.
    */

  trait FUNCTOR[F[_]] {
    def map[A, B](initialValue: F[A])(f: A => B): F[B]
  }

  /** b) applicative
    * @note
    *   describes the ability to `wrap` types. the main api is `pure`. pure can
    *   be thought of as a constructor of a type arg `F`, eg List, Option, etc
    */
  trait APPLICATIVE[F[_]] extends FUNCTOR[F] {
    def pure[A](value: A): F[A]
  }

  /** c) flatMap
    * @note
    *   describes the ability of `chaining` wrapper multiple computations.
    */
  trait FLATMAP[F[_]] extends FUNCTOR[F] {
    def flatMap[A, B](container: F[A])(f: A => F[B]): F[B]
  }

  /** d) monad
    * @note
    *   describes the combination of applicative and flatMap. monad blends the
    *   concepts together to deliver the most unifying foundation of functional
    *   programming
    */
  trait MONAD[F[_]] extends APPLICATIVE[F] with FLATMAP[F] {
    // this is implemented in terms of `pure` and `flatMap`
    override def map[A, B](initialValue: F[A])(f: A => B): F[B] = {
      flatMap(initialValue)(a =>
        /** f(a) === type B, !F[B], so we must wrap it with `pure` === F[B] */
        pure(f(a))
      )
    }
  }

  /** g) traverse
    */
  trait TRAVERSE[F[_]] extends FUNCTOR[F] {
    def traverse[G[_], A, B](container: F[A])(f: A => G[B]): G[F[B]]
  }

  /*
    Big(ger) type class hierarchy in Cats:

      Semigroup -> Monoid

      Semigroupal -> Apply -> FlatMap -->
                  /        \             \
      Functor --->          Applicative -> Monad ---------> MonadError
         |                              \                 /
         |                               ApplicativeError
         |
         ----> Traverse
   */
  def main(args: Array[String]): Unit = {}
}
