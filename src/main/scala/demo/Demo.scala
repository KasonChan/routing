package demo

import actors.{Routee, _}
import akka.actor._
import akka.pattern.ask
import akka.remote.routing.RemoteRouterConfig
import akka.routing._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Created by kasonchan on 1/22/15.
 */
object Demo {
  def main(args: Array[String]) {
    args.size match {
      case 0 => {
        val sHostname = "127.0.0.1"
        val sPort = "2552"
        println(sHostname + ":" + sPort)
      }
      case 2 => {
        val sHostname = args(0)
        val sPort = args(1)
        println(sHostname + ":" + sPort)
      }
      case _ => {
        println("Invalid arguments")
        System.exit(-1)
      }
    }

    val system = ActorSystem("Routers")

    val worker1 = system.actorOf(Props[Routee], "worker1")
    val worker2 = system.actorOf(Props[Routee], "worker2")

    val addresses = Seq(
      Address("akka.tcp", "KeyServerSystem", "127.0.0.1", 2553))
    val server = system.actorOf(
      RemoteRouterConfig(RoundRobinPool(5), addresses).props(Props[Routee]))

    //    ScatterGatherFirstCompletedGroup
    val paths: List[String] = List("/user/worker1", "/user/worker2")
    val configRouter: ActorRef =
      system.actorOf(ScatterGatherFirstCompletedGroup(paths, within = 10.seconds).props(), "configRouter")

    configRouter ! "Hello1"
    configRouter ! "Hello2"
    configRouter ! "Hello3"
    configRouter ! "Hello4"
    configRouter ! "Hello5"

    server ! "Hello5"
    server ! "Hello6"
    server ! "Hello7"

    //    Round robin
    val roundRobinRouter: ActorRef =
      system.actorOf(RoundRobinPool(2).props(Props[Worker]), "roundRobinRouter")

    //    Random pool
    val randomPoolRouter: ActorRef =
      system.actorOf(RandomPool(2).props(Props[Worker]), "randomPoolRouter")

    //    Master round robin
    val masterRouter: ActorRef =
      system.actorOf(Props[Master], "masterRouter")

    masterRouter ! "Hi"

    //    Supervisor smallest mailbox
    val supervisor: ActorRef = system.actorOf(Props[Supervisor], "supervisorRouter")

    //    Broadcast to random pool
    randomPoolRouter ! Broadcast("Broadcast")

    implicit val timeout = Timeout(100 milliseconds)

    val multiple: Future[Int] = ask(masterRouter, Multiple(147, 123)).mapTo[Int]

    //    Blocking await for multiple
    val multipleResult: Int = Await.result(multiple, Duration(100, "millis"))
    println(multipleResult)

    val sum: Future[Int] = ask(roundRobinRouter, Add(3, 4)).mapTo[Int]

    //    Blocking await for sum
    val sumResult: Int = Await.result(sum, Duration(100, "millis"))
    println(sumResult + 5)

    //    Send some works to random pool router
    randomPoolRouter ! Work
    randomPoolRouter ! Work
    randomPoolRouter ! Work
    randomPoolRouter ! Work
    randomPoolRouter ! Work

    //    Send some works to round robin router
    roundRobinRouter ! Work
    roundRobinRouter ! Work
    roundRobinRouter ! Work
    roundRobinRouter ! Work
    roundRobinRouter ! Work

    //    Send some works to supervisor
    supervisor ! Broadcast("Broadcast")

    system.shutdown()
  }
}
