package tree

import scala.annotation.tailrec

sealed trait MySeq {
  def pop: (Tree, MySeq)
  def push(el1: Tree, el2: Tree): MySeq
  def isEmpty: Boolean
}

object MySeq {

  case class MyQueue(front: List[Tree], back: List[Tree]) extends MySeq {
    self =>
    override def pop: (Tree, MySeq) = front match {
      case Nil if back.nonEmpty => MyQueue(back.reverse, Nil).pop
      case Nil                  => (MyNil, self)
      case head :: tail         => (head, MyQueue(tail, back))
    }

    override def push(el1: Tree, el2: Tree): MySeq = MyQueue(front, el2 :: el1 :: back)

    override def isEmpty: Boolean = front.isEmpty && back.isEmpty
  }

  case class MyStack(elements: List[Tree]) extends MySeq {
    self =>

    override def pop: (Tree, MySeq) = elements match {
      case Nil          => (MyNil, self)
      case head :: tail => (head, MyStack(tail))
    }

    override def push(el1: Tree, el2: Tree): MySeq = MyStack(el1 :: el2 :: elements)

    override def isEmpty: Boolean = elements.isEmpty
  }
}

sealed trait Tree

case object MyNil extends Tree

case class Node(value: Int, left: Tree = MyNil, right: Tree = MyNil) extends Tree

object Tree {

  def add(el: Int, tree: Tree): Tree = {
    tree match {
      case Node(value, left, right) if value > el  => Node(value, add(el, left), right)
      case Node(value, left, right) if value <= el => Node(value, left, add(el, right))
      case MyNil                                   => Node(el, MyNil, MyNil)
    }
  }

  def delete(el: Int, tree: Tree): Option[Tree] = {
    unsafe_delete(el, tree) match {
      case MyNil => None
      case tree  => Some(tree)
    }
  }

  def unsafe_delete(el: Int, tree: Tree): Tree = {
    tree match {
      case MyNil => MyNil
      case Node(value, left, right) if el < value =>
        Node(value, unsafe_delete(el, left), right)
      case Node(value, left, right) if el > value =>
        Node(value, left, unsafe_delete(el, right))
      case Node(value, left, right) =>
        right match {
          case MyNil => MyNil
          case node: Node =>
            val minRight = findMin(node)
            Node(minRight, left, unsafe_delete(minRight, right))
        }
    }
  }

  @tailrec
  private def findMin(tree: Node): Int = {
    tree.left match {
      case MyNil                    => tree.value
      case Node(value, left, right) => findMin(Node(value, left, right))
    }
  }

  @tailrec
  private def foldLeft[T](seq: MySeq)(acc: T)(f: (T, Node) => T): T = {
    if (seq.isEmpty) acc
    else {
      val (tree, newSeq) = seq.pop
      tree match {
        case MyNil                           => foldLeft(newSeq)(acc)(f)
        case node @ Node(value, left, right) => foldLeft(newSeq.push(left, right))(f(acc, node))(f)
      }
    }
  }

  def breadthFirstSearch[T](tree: Tree)(acc: T)(f: (T, Node) => T): T = {
    foldLeft(MySeq.MyQueue(List(tree), List.empty))(acc)(f)
  }

  def depthFirstSearch[T](tree: Tree)(acc: T)(f: (T, Node) => T): T = {
    foldLeft(MySeq.MyStack(List(tree)))(acc)(f)
  }

  def max(tree: Tree)(f: Tree => Int => ((Int, Node) => Int) => Int): Option[Int] = {
    f(tree)(Int.MinValue) { (a: Int, b: Node) => math.max(a, b.value) } match {
      case Int.MinValue => None
      case num          => Some(num)
    }
  }

  def min(tree: Tree)(f: Tree => Int => ((Int, Node) => Int) => Int): Option[Int] = {
    f(tree)(Int.MaxValue) { (a: Int, b: Node) => math.min(a, b.value) } match {
      case Int.MaxValue => None
      case num          => Some(num)
    }
  }

  def size(tree: Tree): Int = {
    foldLeft(MySeq.MyStack(List(tree)))(0) { (a: Int, _) => a + 1 }
  }

  def printTree(tree: Tree): Unit = {
    @tailrec
    def loop(currentLevel: List[Tree]): Unit = {
      val nodes = currentLevel.collect { case node: Node => node }
      if (nodes.nonEmpty) {
        nodes.foreach(node => print(s"${node.value} "))
        println()
        val nextLevel = nodes.flatMap(node => List(node.left, node.right))
        loop(nextLevel)
      }
    }
    loop(List(tree))
  }

  def main(args: Array[String]): Unit = {
    val tree1 = Node(3, Node(2, Node(3)), Node(5, Node(4), Node(6, Node(4), Node(3))))
    val seq = MySeq.MyQueue(List(tree1), List.empty)
    println(foldLeft(seq)(0) { (a: Int, b: Node) => a + b.value })
    println(max(tree1)(breadthFirstSearch))
    printTree(tree1)
  }
}
