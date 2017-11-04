package com.marimon.dhondt

import org.scalajs.dom
import org.scalajs.dom.document

import scala.collection.immutable.Seq

case class Votes(party: String, count: Int) {
  require(count > 0)
}

case class Coalition(name: String, parties: Set[Votes]) {
  val coalitionVotes = parties.map(_.count).sum
}

object Coalition {
  def apply(party: String, count: Int): Coalition =
    Coalition(party, Set(Votes(party, count)))
}

case class Seats(name: String, seatCount: Int)


object MainApp {
  def main(args: Array[String]): Unit = {
    val votesGirona = Set(
      Coalition("JxSí", 216333),
      Coalition("Cs", 48346),
      Coalition("PSC", 33416),
      Coalition("CUP", 33117),
      Coalition("PP", 22660),
      Coalition("CatSíqueesPot", 18399)
    )

    val gironaDistribution: Seq[Seats] = distribute("Girona", 17, votesGirona)

    appendDistribution(document.body, gironaDistribution)
  }

  def distribute(district: String, seatsToDistribute: Int, coalitions: Set[Coalition]): Seq[Seats] = {
    def genValues(value: Double, coeficient: Int): List[Double] = {
      if (coeficient == seatsToDistribute) value :: Nil
      else {
        val actual = if (coeficient == 0) value else (value - value / (1 + coeficient))
        actual :: genValues(actual, coeficient + 1)
      }
    }

    coalitions.toSeq.flatMap { c =>
      for {
        value <- genValues(c.coalitionVotes, 0)
      } yield (c.name, value)
    }
      .sortWith(_._2 > _._2)
      .take(seatsToDistribute)
      .map(_._1)
      .groupBy(x => x)
      .mapValues(_.length)
      .toList
      .sortWith(_._2 > _._2)
      .map { case (n, c) => Seats(n, c) }
  }

  def appendDistribution(targetNode: dom.Node, seats: Seq[Seats]): Unit = {
    val parNode = document.createElement("p")
    val list = document.createElement("ul")
    seats.foreach(s => {
      val ul = document.createElement("li")
      val res = document.createTextNode(s"${s.name}=${s.seatCount}")
      ul.appendChild(res)
      list.appendChild(ul)
    })
    parNode.appendChild(list)
    targetNode.appendChild(parNode)
  }

}
