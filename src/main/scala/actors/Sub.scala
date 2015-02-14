package actors

import akka.actor.Actor

/**
 * Created by kasonchan on 2/14/15.
 */
class Sub extends Actor with akka.actor.ActorLogging {
  def receive = {
    case m: String => {
      log.info(m)
    }
    case x => {
      log.warning(x.toString)
    }
  }
}
