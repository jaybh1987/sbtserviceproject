import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import spray.json._

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Success

object WebServer {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: Materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher

    //domain model
    final case class Order(email: String, money: Double)
    implicit val orderFormat = jsonFormat2(Order)

      val route = concat(


        path("orders"){
          post {
              entity(as[Order]){
                order: Order =>
                    val a = DatabaseClass.testInsertAction.map { int =>
                      println(order)
                      println(int)
                    }.recover {
                      case t => {
                        println(t.getMessage)
                        Future.successful(0L)
                      }
                    }

                    onComplete(a) {
                      case Success(v) => complete(StatusCodes.OK)
                      case _ => complete(StatusCodes.InternalServerError)
                    }
                   complete(order.toJson)
              }
          }
        })


      val bindingFutures = Http().bindAndHandle(route, "localhost", 9090)


      println(s"Server online at http://localhost:9090/\n press RETURN to stop....")

      StdIn.readLine()  //let it run until user presses return

      bindingFutures
        .flatMap(r => r.unbind()) //trigger unbinding from the port
        .onComplete( r => system.terminate()) //and shutdown when done.


  }

//    case class Color(red: Int, green: Int, blue: Int)
//    val route = path("color") {
//      parameters('red.as[Int] , 'green.as[Int], 'blue.as[Int]).as(Color) { color =>
//        color
//        complete(s"color instance $color")
//      }
//    }

//    val route = get {
//      concat(
//        pathSingleSlash{
//          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
//            "<html><body><h3>Hello World.</h3></body></html>"))
//        },
//        path("ping") {
//          complete("PONG!")
//        },
//        path("crash") {
//          sys.error("BOOM!")
//        },
//        pathPrefix("order"/ IntNumber) {
//          oid =>
//            println("------------------>>>"+oid)
//            complete("ok")
//        }
//      )
//    }

    //route will be implicitly converted to Flow using RouteResult.route2HandlerFlow






}