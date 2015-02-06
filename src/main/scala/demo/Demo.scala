package demo

import actors.{Work, Worker}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.{RandomPool, RoundRobinPool}

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

    for (i <- 1 to 5)
      roundRobinRouter ! Work

    for (i <- 1 to 5)
      randomPoolRouter ! Work

    system.shutdown()
  }
}
