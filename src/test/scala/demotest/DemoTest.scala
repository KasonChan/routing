package demotest

import actors.Worker
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
 * Created by kasonchan on 2/5/15.
 */
class DemoTest extends WordSpecLike with Matchers with BeforeAndAfterAll {
  val system = ActorSystem("Routers")

  val RoundRobinRouter: ActorRef =
    system.actorOf(RoundRobinPool(2).props(Props[Worker]), "RoundRobinRouter")

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  //  TODO: Add test cases
}