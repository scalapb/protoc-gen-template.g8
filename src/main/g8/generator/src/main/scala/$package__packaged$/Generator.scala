package $package$

import com.google.protobuf.CodedInputStream
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Descriptors._
import com.google.protobuf.compiler.PluginProtos.{CodeGeneratorResponse, CodeGeneratorRequest}
import scala.collection.JavaConverters._

import scalapb.compiler.{DescriptorImplicits, FunctionalPrinter}
import scalapb.options.compiler.Scalapb

/** This is the interface that code generators need to implement. */
object Generator extends protocbridge.ProtocCodeGenerator {

  // This would make sbt-protoc append the following artifacts to the user's
  // project.  If you have a runtime library this is the place to specify it.
  override def suggestedDependencies: Seq[protocbridge.Artifact] = Nil

  override def run(req: Array[Byte]): Array[Byte] = run(CodedInputStream.newInstance(req))

  def run(input: CodedInputStream): Array[Byte] = {
    val registry = ExtensionRegistry.newInstance()
    Scalapb.registerAllExtensions(registry)
    val request = CodeGeneratorRequest.parseFrom(input)
    val b = CodeGeneratorResponse.newBuilder

    scalapb.compiler.ProtobufGenerator.parseParameters(request.getParameter) match {
      case Right(params) =>
        try {
          val fileDescByName: Map[String, FileDescriptor] =
            request.getProtoFileList.asScala.foldLeft[Map[String, FileDescriptor]](Map.empty) {
              case (acc, fp) =>
                val deps = fp.getDependencyList.asScala.map(acc)
                acc + (fp.getName -> FileDescriptor.buildFrom(fp, deps.toArray))
            }

          val implicits = new DescriptorImplicits(params, fileDescByName.values.toVector)
          val generator = new FileGenerator(implicits)
          request.getFileToGenerateList.asScala.foreach {
            name =>
              val fileDesc = fileDescByName(name)
              val responseFile = generator.generateFile(fileDesc)
              b.addFile(responseFile)
          }
          b.build.toByteArray
        }
        catch {
          case e: Throwable => b.setError(e.getMessage)
        }
      case Left(error) =>
        b.setError(error)
    }
    b.build().toByteArray
  }
}