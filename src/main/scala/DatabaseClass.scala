import slick.jdbc.JdbcBackend
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object DatabaseClass extends App{


  println(s"hello world.")

  val db = Database.forURL(
    "jdbc:postgresql://localhost/postgres",
    user = "postgres",
    driver = "org.postgresql.Driver")

  class Users(tag: Tag) extends Table[(Long, String)] (tag,"users") {
    def id = column[Long]("id", O.PrimaryKey)
    def username = column[String]("username")
    def * = (id, username)
  }

  val users = TableQuery[Users]


//  try {
//
//
//  } finally {
//    db.close()
//  }


}
