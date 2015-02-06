package actors

import akka.actor.Actor

/**
 * Created by kasonchan on 1/22/15.
 */
class Worker extends Actor with akka.actor.ActorLogging {
  def receive = {
    case Work => {
      log.info("Works")
    }
    case m: String => {
      log.info(m)
    }
    case Add(x, y) => {
      log.info(Add(x, y).toString)
      sender() ! add(x, y)
    }
    case _ => log.warning("Unknown message")
  }

  def add(x: Int, y: Int) = x + y

  def multiple(x: Int, y: Int) = x * y
}
