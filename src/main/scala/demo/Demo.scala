package demo

import actors.{Add, Worker}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.{RandomPool, RoundRobinPool}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Created by kasonchan on 1/22/15.
 */
object Demo {
  def main(args: Array[String]) {
    val system = ActorSystem("Routers")

    // Round robin
    val roundRobinRouter: ActorRef =
      system.actorOf(RoundRobinPool(2).props(Props[Worker]), "roundRobinRouter")

    // Random pool
    val randomPoolRouter: ActorRef =
      system.actorOf(RandomPool(2).props(Props[Worker]), "randomPoolRouter")

    implicit val timeout = Timeout(100 milliseconds)

    val sum: Future[Int] = ask(roundRobinRouter, Add(3, 4)).mapTo[Int]

    //    Blocking await for sum
    val result: Int = Await.result(sum, Duration(100, "millis"))
    println(result + 5)

    system.shutdown()
  }
}
