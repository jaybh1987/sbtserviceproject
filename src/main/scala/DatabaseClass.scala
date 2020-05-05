import slick.jdbc.JdbcBackend
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object DatabaseClass extends App{

  println(s"hello world.")

  val db = Database.forURL(
    "jdbc:postgresql://localhost/postgres",
    user = "postgres",
    password = "postgres",
    driver = "org.postgresql.Driver",
    keepAliveConnection = true)



  final case class Users(id: Long = 0l, username: String)

  final case class UsersTable(tag: Tag) extends Table[Users](tag, "users") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def * = (id, username).mapTo[Users]
  }

  val users = TableQuery[UsersTable]

  try {

    println(s"---------> ${users.schema.createStatements.mkString}")

    def freshTestData = Seq(
      Users(0, "amit bhavsar"),
      Users(0, "amit patel"),
      Users(0, "amit parekh"),
      Users(0, "amit parikh")
    )

    def findBhavsar = users.filter(_.username.like("Bhavsar")).map(r => r).result

    val r = db.run(findBhavsar)

//    val insert : DBIO[Option[Int]] = users ++= freshTestData
//
//    val result: Future[Option[Int]] = db.run(insert)
//    val rowCount = Await.result(result, 4.seconds)



//    val action: DBIO[Unit] = users.schema.create
//
//    val future: Future[Unit] = db.run(action)
//
//    val result = Await.result(future, 5.seconds)

    println(s"result -----------__> ${rowCount}")

  } finally {
    db.close()
  }


}
