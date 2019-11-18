package $package$

import com.google.protobuf.CodedInputStream

object Main {
  def main(args: Array[String]): Unit = {
    System.out.write(Generator.run(CodedInputStream.newInstance(System.in)))
  }
}

