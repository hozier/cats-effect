package com.rockthejvm.part1recap

<<<<<<< HEAD
/** @note
  *   Covers: 1 - given instances, 2 - extension methods, 3 - combining
  *   given/using to implement type classes
  */
object ContextualAbstractionsScala3 {
  def main(args: Array[String]): Unit = {

    /** @note
      *   1 - given instances are equiv to implicit args/value from scala 2
      */
    def increment: (Int => (Int => Int)) = x => (amount) => x + amount
    val twelve = increment(10)(2)

    /** given/using pairs allow us to anchor an argument value to a function
      * when `given` is applied, the value declared is automatically passed by
      * the compiler to the `using` variable
      */
    given defaultAmount: Int = 10
    def incrementWithUsing(x: Int)(using amount: Int): Int = x + amount
    val thirteen = incrementWithUsing(3)
    println(s"thirteen=$thirteen")

    /** @example start ****************************************************** */
    /** Combiner overview: Display by the example which follows how to `attach`
      * functionality to certain types. Follow up with displaying composability
      * of attachments created.
      */

    /** the standalone version of a monoid implemented. below is the standard
      * way of processing or reducing a list with some initial seed and some
      * combination function
      */
    trait Combiner[A] {
      def combine(x: A, y: A): A
      def empty: A
    }

    def combineAll[A](values: List[A])(using combiner: Combiner[A]): A = {
      values.foldLeft(combiner.empty)(combiner.combine)
    }

    /** combineAll is available for Lists of any type, for which we have a
      * `given` `Combiner` in scope, eg, `given intCombiner: Combiner[Int] with
      * {...}`
      */
    given intCombiner: Combiner[Int] with {
      override def combine(x: Int, y: Int) = x + y
      override def empty = 0
    }

    /** @note
      *   execute on `combineAll` List[Int] example
      * @description
      *   The List[Int] sent in below is passed to the defined instance of
      *   `Combiner` (that the compiler requires). This instance known as
      *   `intCombiner` is passed automatically and leveraged internally by
      *   combineAll.
      */
    println(combineAll((1 to 10).toList)) /* so. so. cool! */

    /** @example abstract through composition ***************************** */
    // synthesize given instances
    given optionCombiner[T](using combiner: Combiner[T]): Combiner[Option[T]]
      with {
      override def empty = Some(combiner.empty)
      override def combine(x: Option[T], y: Option[T]): Option[T] = for {
        /** unpacks options and then passes them to combine */
        vx <- x
        vy <- y
      } yield combiner.combine(vx, vy)
    }

    println(
      combineAll(List(Option(1), Some(2), Some(3)))
    ) /* even. cooler! */
    /** @example end ****************************************************** */

=======
object ContextualAbstractionsScala3 {

  // given/using combo
  def increment(x: Int)(using amount: Int): Int = x + amount
  given defaultAmount: Int = 10
  val twelve = increment(2) // (10) automatically by the compiler

  def multiply(x: Int)(using factor: Int): Int = x * factor
  val aHundred = multiply(10) // defaultAmount is passed automatically

  // more complex use case
  trait Combiner[A] {
    def combine(x: A, y: A): A
    def empty: A
  }

  def combineAll[A](values: List[A])(using combiner: Combiner[A]): A =
    values.foldLeft(combiner.empty)(combiner.combine)

  given intCombiner: Combiner[Int] with {
    override def combine(x: Int, y: Int) = x + y
    override def empty = 0
  }

  val numbers = (1 to 10).toList
  val sum10 = combineAll(numbers) // intCombiner passed automatically

  // synthesize given instances
  given optionCombiner[T](using combiner: Combiner[T]): Combiner[Option[T]] with {
    override def empty = Some(combiner.empty)
    override def combine(x: Option[T], y: Option[T]): Option[T] = for {
      vx <- x
      vy <- y
    } yield combiner.combine(vx, vy)
  }

  val sumOptions: Option[Int] = combineAll(List(Some(1), None, Some(2)))

  // extension methods
  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name"
  }

  extension (name: String)
    def greet(): String = Person(name).greet()

  val alicesGreeting = "Alice".greet()

  // generic extension
  extension [T](list: List[T])
    def reduceAll(using combiner: Combiner[T]): T =
      list.foldLeft(combiner.empty)(combiner.combine)

  val sum10_v2 = numbers.reduceAll


  def main(args: Array[String]): Unit = {

  }
}

object TypeClassesScala3 {
  case class Person(name: String, age: Int)

  // type classes

  // part 1 - Type class definition
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  // part 2 - type class instances
  given stringSerializer: JSONSerializer[String] with {
    override def toJson(value: String) = "\"" + value + "\""
  }

  given intSerializer: JSONSerializer[Int] with {
    override def toJson(value: Int) = value.toString
  }

  given personSerializer: JSONSerializer[Person] with {
    override def toJson(person: Person) =
      s"""
         |{"name": "${person.name}", "age": ${person.age}}
         |""".stripMargin.trim
  }

  // part 3 - user-facing API
  def convert2Json[T](value: T)(using serializer: JSONSerializer[T]): String =
    serializer.toJson(value)

  def convertList2Json[T](list: List[T])(using serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  // part 4 - extension methods just for the types we support
  extension [T](value: T)
    def toJson(using serializer: JSONSerializer[T]): String =
      serializer.toJson(value)

  def main(args: Array[String]): Unit = {
    println(convertList2Json(List(Person("Alice", 23), Person("Bob", 46))))
    val bob = Person("Bob", 46)
    println(bob.toJson)
>>>>>>> c015821ffc5c71666e08b5284c9fca7de7f249fc
  }
}
