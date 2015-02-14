package actors

import akka.actor.{ActorRef, Actor, Props}
import akka.routing._

/**
 * Created by kasonchan on 2/13/15.
 */
class Controller extends Actor with akka.actor.ActorLogging {
  var router: Router = {
    val routees: Vector[ActorRefRoutee] = Vector.fill(5) {
      val r: ActorRef = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case m: String => {
      router.route(m, sender())
    }
    case x => {
      log.warning(x.toString)
    }
  }
}
