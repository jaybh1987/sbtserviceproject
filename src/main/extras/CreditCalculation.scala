


val credit: Double = 500

case class Model(price: Double)

def updateCredit(credit: Double, modelPrice: Double): Double ={

  if(credit > modelPrice) credit - modelPrice else credit
}

val seq = Seq(
  Model(100),
  Model(200),
  Model(100),
  Model(300))

val seqtesttwo = Seq(
  Model(200),
  Model(50)
)

var c = credit
val avaicredit = 0

val cal = seq.map { m =>




  if(m.price <= c) {
    c = c - m.price
    m
  } else if(m.price > c && c > 0) {
    Model(c)
  }
  else Model(c)
}


val availcredit = credit - cal.map(_.price).sum

println(s"avaialble credit = $c")