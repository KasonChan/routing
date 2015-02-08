package demotest

import actors.{Add, Multiple, Worker}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.{RandomPool, RoundRobinPool}
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, ShouldMatchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by kasonchan on 2/5/2015.
 */
class DemoTest(system: ActorSystem) extends TestKit(system) with WordSpecLike with ShouldMatchers with BeforeAndAfterAll {
  // Round robin
  val roundRobinRouter: ActorRef =
    system.actorOf(RoundRobinPool(2).props(Props[Worker]), "roundRobinRouter")
  // Random pool
  val randomPoolRouter: ActorRef =
    system.actorOf(RandomPool(2).props(Props[Worker]), "randomPoolRouter")

  def this() = this(ActorSystem("Router"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "ask(roundRobinRouter, Add(3, 4)).mapTo[Int]" should {
    "= 7" in {
      implicit val timeout = Timeout(100 milliseconds)
      val sum = ask(roundRobinRouter, Add(3, 4)).mapTo[Int]
      val result: Int = Await.result(sum, Duration(100, "milliseconds"))
      result should be(7)
    }
  }

  "ask(randomPoolRouter, Multiple(8, 7)).mapTo[Int]" should {
    "= 56" in {
      implicit val timeout = Timeout(100 milliseconds)
      val sum = ask(randomPoolRouter, Multiple(8, 7)).mapTo[Int]
      val result: Int = Await.result(sum, Duration(100, "milliseconds"))
      result should be(56)
    }
  }
}
