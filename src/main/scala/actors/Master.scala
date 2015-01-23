package actors

import akka.actor.{Actor, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

/**
 * Created by kasonchan on 1/22/15.
 * Last modified by kasonchan on 1/22/15.
 */
class Master extends Actor with akka.actor.ActorLogging {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case w: Work => {
      log.info("Works")
      router.route(w, sender())
    }
    case m: String =>
      log.info(m)
      router.route(m, sender())
  }
}
