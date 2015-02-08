package actors

import akka.actor.Actor

/**
 * Created by kasonchan on 1/22/15.
 */
class Worker extends Actor with akka.actor.ActorLogging {
  def receive = {
    case Work => {
      log.info("Works")
      sender() ! Work
    }
    case m: String => {
      log.info(m)
    }
    case Add(x: Int, y: Int) => {
      log.info("Add(" + x + ", " + y + ")")
      sender() ! add(x, y)
    }
    case Multiple(x: Int, y: Int) => {
      log.info("Multiple(" + x + ", " + y + ")")
      sender() ! multiple(x, y)
    }
    case _ => log.warning("Unknown message")
  }

  def add(x: Int, y: Int): Int = x + y

  def multiple(x: Int, y: Int): Int = x * y
}
