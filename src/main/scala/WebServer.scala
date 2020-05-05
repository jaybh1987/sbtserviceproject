import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object WebServer {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher


    val route = get {
      concat(
        pathSingleSlash{
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
            "<html><body><h3>Hello World.</h3></body></html>"))
        },
        path("ping") {
          complete("PONG!")
        },
        path("crash") {
          sys.error("BOOM!")
        }
      )
    }

    //route will be implicitly converted to Flow using RouteResult.route2HandlerFlow
    val bindingFutures = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\n press RETURN to stop....")

    StdIn.readLine()  //let it run until user presses return

    bindingFutures
      .flatMap(r => r.unbind()) //trigger unbinding from the port
      .onComplete( r => system.terminate()) //and shutdown when done.

  }
}