package tree

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TreeSpec extends AnyFlatSpec with Matchers {

  def inorder(tree: Tree): List[Int] = tree match {
    case MyNil                    => List()
    case Node(value, left, right) => inorder(left) ++ List(value) ++ inorder(right)
  }

  def bfsTraversal(tree: Tree): List[Int] = {
    Tree.breadthFirstSearch[List[Int]](tree)(List()) { (acc, node) =>
      acc :+ node.value
    }
  }

  def dfsTraversal(tree: Tree): List[Int] = {
    Tree.depthFirstSearch[List[Int]](tree)(List()) { (acc, node) =>
      acc :+ node.value
    }
  }

  "add" should "add elements to the tree maintaining BST property" in {
    val emptyTree: Tree = MyNil
    val tree1 = Tree.add(5, emptyTree)
    inorder(tree1) shouldEqual List(5)

    val tree2 = Tree.add(3, tree1)
    inorder(tree2) shouldEqual List(3, 5)

    val tree3 = Tree.add(7, tree2)
    inorder(tree3) shouldEqual List(3, 5, 7)

    val tree4 = Tree.add(2, tree3)
    inorder(tree4) shouldEqual List(2, 3, 5, 7)

    val tree5 = Tree.add(4, tree4)
    inorder(tree5) shouldEqual List(2, 3, 4, 5, 7)
  }

  "delete" should "remove elements from the tree maintaining BST property" in {
    val emptyTree: Tree = MyNil
    val tree = Tree.add(5, emptyTree)
    val tree1 = Tree.add(3, tree)
    val tree2 = Tree.add(7, tree1)
    val tree3 = Tree.add(2, tree2)
    val tree4 = Tree.add(4, tree3)

    val x = inorder(tree4)
    inorder(tree4) shouldEqual List(2, 3, 4, 5, 7)

    val treeAfterDelete = Tree.delete(3, tree4).get
    inorder(treeAfterDelete) shouldEqual List(2, 4, 5, 7)

    val treeAfterDelete2 = Tree.delete(5, treeAfterDelete).get
    inorder(treeAfterDelete2) shouldEqual List(2, 4, 7)

    val treeAfterDelete3 = Tree.delete(10, treeAfterDelete2).get
    inorder(treeAfterDelete3) shouldEqual List(2, 4, 7) // Удаление несуществующего элемента не происходит
  }

  "delete" should "return None when the tree becomes empty after deletion" in {
    val tree = Tree.add(5, MyNil)
    val result = Tree.delete(5, tree)
    result shouldBe None
  }

  "breadthFirstSearch" should "traverse the tree in BFS order" in {
    val emptyTree: Tree = MyNil
    val tree = Tree.add(5, emptyTree)
    val tree1 = Tree.add(3, tree)
    val tree2 = Tree.add(7, tree1)
    val tree3 = Tree.add(2, tree2)
    val tree4 = Tree.add(4, tree3)
    // Дерево имеет структуру:
    //        5
    //       / \
    //      3   7
    //     / \
    //    2   4
    // BFS обход должен быть: 5, 3, 7, 2, 4

    val bfsResult = bfsTraversal(tree4)
    bfsResult shouldEqual List(5, 3, 7, 2, 4)
  }

  "depthFirstSearch" should "traverse the tree in DFS order" in {
    val emptyTree: Tree = MyNil
    val tree = Tree.add(5, emptyTree)
    val tree1 = Tree.add(3, tree)
    val tree2 = Tree.add(7, tree1)
    val tree3 = Tree.add(2, tree2)
    val tree4 = Tree.add(4, tree3)
    // DFS обход должен быть: 5, 3, 2, 4, 7

    val dfsResult = dfsTraversal(tree4)
    dfsResult shouldEqual List(5, 3, 2, 4, 7)
  }

  "max" should "return the maximum value in the tree" in {
    val emptyTree: Tree = MyNil
    val tree = Tree.add(5, emptyTree)
    val tree1 = Tree.add(3, tree)
    val tree2 = Tree.add(7, tree1)
    val tree3 = Tree.add(2, tree2)
    val tree4 = Tree.add(4, tree3)
    val tree5 = Tree.add(10, tree4)
    val tree6 = Tree.add(1, tree5)

    val maxValue = Tree.max(tree6)(Tree.breadthFirstSearch)
    maxValue shouldEqual Some(10)
  }

  "min" should "return the minimum value in the tree" in {
    val emptyTree: Tree = MyNil
    val tree = Tree.add(5, emptyTree)
    val tree1 = Tree.add(3, tree)
    val tree2 = Tree.add(7, tree1)
    val tree3 = Tree.add(2, tree2)
    val tree4 = Tree.add(4, tree3)
    val tree5 = Tree.add(10, tree4)
    val tree6 = Tree.add(1, tree5)

    val minValue = Tree.min(tree6)(Tree.breadthFirstSearch)
    minValue shouldEqual Some(1)
  }

  "size" should "return the number of nodes in the tree" in {
    val emptyTree: Tree = MyNil
    val tree = Tree.add(5, emptyTree)
    val tree1 = Tree.add(3, tree)
    val tree2 = Tree.add(7, tree1)
    val tree3 = Tree.add(2, tree2)
    val tree4 = Tree.add(4, tree3)
    val tree5 = Tree.add(10, tree4)
    val tree6 = Tree.add(1, tree5)

    val treeSize = Tree.size(tree6)
    treeSize shouldEqual 7
  }
}
