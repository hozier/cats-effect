package com.rockthejvm.part1recap

object CatsTypeClasses {
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
