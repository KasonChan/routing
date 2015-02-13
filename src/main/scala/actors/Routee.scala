package actors

import akka.actor.Actor

/**
 * Created by kasonchan on 2/12/15.
 */
class Routee extends Actor with akka.actor.ActorLogging {
  override def preStart(): Unit = {
    log.info("Pre-start")
  }

  override def postStop(): Unit = {
    log.info("Post-stop")
  }

  def receive = {
    case Work => {
      log.info("Works")
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
