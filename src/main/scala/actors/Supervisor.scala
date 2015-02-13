package actors

import akka.actor.{Terminated, Actor, Props}
import akka.routing._

/**
 * Created by kasonchan on 2/12/15.
 */
class Supervisor extends Actor with akka.actor.ActorLogging {
  var router = {
    val routees = Vector.fill(5) {
      val routee = context.actorOf(Props[Routee])
      context watch routee
      ActorRefRoutee(routee)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  def receive = {
    case Broadcast(s: String) => {
      log.info(s)
      router.route(Broadcast(s), sender())
    }
    case Broadcast(Work) => {
      log.info("Works")
      router.route(Broadcast(Work), sender())
    }
    case Work => {
      log.info("Works")
      router.route(Work, sender())
    }
    case Add(x: Int, y: Int) => {
      log.info("Add(" + x + ", " + y + ")")
      router.route(Add(x, y), sender())
    }
    case Multiple(x: Int, y: Int) => {
      log.info("Add(" + x + ", " + y + ")")
      router.route(Multiple(x, y), sender())
    }
    case m: String => {
      log.info(m)
      router.route(m, sender())
    }
    case x => {
      log.warning(x.toString)
    }
  }
}
