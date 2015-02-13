package actors

case object Work

case object Kill

case class Add(x: Int, y: Int)

case class Multiple(x: Int, y: Int)

case class Result(i: Int)
