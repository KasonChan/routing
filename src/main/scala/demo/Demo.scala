package demo

import actors.{Add, Master, Worker}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
 * Created by kasonchan on 1/22/15.
 * Last modified by kasonchan on 1/22/15.
 */
object Demo {
  def main(args: Array[String]) {
    val system = ActorSystem("Routers")

    val router1: ActorRef =
      system.actorOf(RoundRobinPool(2).props(Props[Worker]), "router1")

    val router2: ActorRef =
      system.actorOf(RoundRobinPool(2).props(Props[Master]), "router2")

    //    router1 ! Work()
    //
    //    router1 ! "What's up"
    //
    //    router1 ! Broadcast("first message")
    //
    //    router2 ! Broadcast("2 msg")

    val d1 = 1 to 5 toArray
    val d2 = (1 to 5 toArray) reverse

    implicit val timeout = Timeout(5.second)

    val r: Array[Future[Int]] = {
      for {
        x <- d1
        y <- d2
        if (d1.indexWhere(_ == x) == d2.indexWhere(_ == y))
      }
      yield ask(router1, Add(x, y)).mapTo[Int]
    }

    for (d <- r) {
      d onComplete {
        case Success(c: Int) => println(c)
        case Failure(t) => println("An error has occured: " + t.getMessage)
      }
    }

    system.shutdown()
  }
}
