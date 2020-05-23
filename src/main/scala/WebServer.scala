import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import scala.io.StdIn

object WebServer {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: Materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher

    //domain model
    final case class Order(email: String, money: Double)
    implicit val orderFormat = jsonFormat2(Order)


      val route = concat(
        path("orders") {
          post {
            decodeRequest {
              entity(as[Order]) {
                order: Order =>

                  println(s"order => $order")
                  complete("ok")
              }
            }
          }
        }
      )


      val bindingFutures = Http().bindAndHandle(route, "localhost", 8080)


      println(s"Server online at http://localhost:8080/\n press RETURN to stop....")

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