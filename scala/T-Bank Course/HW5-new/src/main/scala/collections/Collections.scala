package collections

import scala.annotation.tailrec
import scala.collection.mutable

object Collections {

  /*
    In a sorted list find all pairs of two neighbor numbers which have a gap between them
    None for Seq(1, 2, 3, 4)
    Some(Seq((2, 8))) for Seq(1, 2, 8)
    Some(Seq((3, 5), (5, 7))) for Seq(3, 5, 7)
   */
  def findGaps(l: Seq[Int]): Option[Seq[(Int, Int)]] = {
    val seq = l.init.zip(l.tail).filter { case (a, b) => a + 1 != b }
    if (seq.isEmpty) None else Some(seq)
  }

  /*
    Find key-value pair with the minimum value in the map
    try to implement min in different ways (fold, reduce, recursion)
   */
  def minFold(map: Map[String, Int]): Option[(String, Int)] = {
    map.foldLeft(Option.empty[(String, Int)]) {
      case (None, entry)                                 => Some(entry)
      case (Some((_, minValue)), (k, v)) if v < minValue => Some((k, v))
      case (acc, _)                                      => acc
    }
  }

  def minReduce(map: Map[String, Int]): Option[(String, Int)] = {
    map.reduceOption((x, y) => if (x._2 > y._2) y else x)
  }

  def minRecursion(map: Map[String, Int]): Option[(String, Int)] = {
    @tailrec
    def recFunc(map: Map[String, Int])(acc: (String, Int)): (String, Int) = {
      map match {
        case _ if map.isEmpty          => acc
        case _ if map.head._2 < acc._2 => recFunc(map.tail)(map.head)
        case _                         => recFunc(map.tail)(acc)
      }
    }
    map match {
      case _ if map.isEmpty => None
      case _                => Some(recFunc(map.tail)(map.head))
    }
  }

  // Implement scanLeft - running total, applying [f] to elements of [list] (not using scans ofc)
  def scanLeft[T](zero: T)(list: Seq[T])(f: (T, T) => T): Seq[T] = {
    @tailrec
    def recFunc(zero: T, list: Seq[T], ans_list: Seq[T]): Seq[T] = {
      list.headOption match {
        case None => ans_list.appended(zero)
        case _    => recFunc(f(zero, list.head), list.tail, ans_list.appended(zero))
      }
    }
    recFunc(zero, list, Seq())
  }

  // Count the consistent occurences of each character in the string
  def count(s: String): List[(Char, Int)] = {
    s.foldLeft(Map.empty[Char, Int]) { (acc, char) =>
      acc + (char -> (acc.getOrElse(char, 0) + 1))
    }.toList
  }

  def main(args: Array[String]): Unit = {
    val map = Map("a" -> 1)
    println(Collections.minFold(map))
  }

}
