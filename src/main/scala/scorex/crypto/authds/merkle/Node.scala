package scorex.crypto.authds.merkle

import scorex.crypto.encode.Base58
import scorex.crypto.hash.CommutativeHash

trait Node {
  def hash: Array[Byte]
}

case class InternalNode(left: Node, right: Node)
                       (implicit val hf: CommutativeHash[_]) extends Node {
  override lazy val hash: Array[Byte] = right match {
    case EmptyNode => left.hash
    case n: Node => hf.prefixedHash(1: Byte, left.hash, right.hash)
  }

  override def toString: String = s"InternalNode(${Base58.encode(left.hash)}, ${Base58.encode(right.hash)})"
}

case class Leaf(data: Array[Byte])
               (implicit val hf: CommutativeHash[_]) extends Node {
  override lazy val hash = hf.prefixedHash(0: Byte, data)

  override def toString: String = s"Leaf(${Base58.encode(hash)})"

}

case object EmptyNode extends Node {
  override val hash: Array[Byte] = Array[Byte]()
}
