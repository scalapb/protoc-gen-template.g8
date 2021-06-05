package $package$.compiler

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Descriptors._
import protocbridge.Artifact
import protocgen.{CodeGenApp, CodeGenResponse, CodeGenRequest}
import scalapb.compiler.{DescriptorImplicits, FunctionalPrinter, ProtobufGenerator}
import scalapb.options.compiler.Scalapb
import scala.collection.JavaConverters._

object CodeGenerator extends CodeGenApp {
  override def registerExtensions(registry: ExtensionRegistry): Unit = {
    Scalapb.registerAllExtensions(registry)
  }

  // When your code generator will be invoked from SBT via sbt-protoc, this will add the following
  // artifact to your users build whenver the generator is used in `PB.targets`:
  override def suggestedDependencies: Seq[Artifact] =
    Seq(
      Artifact(
        BuildInfo.organization,
        "$name;format="norm"$-core",
        BuildInfo.version,
        crossVersion = true
      )
    )

  // This is called by CodeGenApp after the request is parsed.
  def process(request: CodeGenRequest): CodeGenResponse =
    ProtobufGenerator.parseParameters(request.parameter) match {
      case Right(params) =>

        // Implicits gives you extension methods that provide ScalaPB names and types
        // for protobuf entities.
        val implicits =
          DescriptorImplicits.fromCodeGenRequest(params, request)

        // Process each top-level message in each file.
        // This can be customized if you want to traverse the input in a different way.
        CodeGenResponse.succeed(
          for {
            file <- request.filesToGenerate
            message <- file.getMessageTypes().asScala
          } yield new MessagePrinter(message, implicits).result
      )
      case Left(error)   =>
        CodeGenResponse.fail(error)
    }
}

class MessagePrinter(message: Descriptor, implicits: DescriptorImplicits) {
  import implicits._

  private val MessageObject =
    message.scalaType.sibling(message.scalaType.name + "FieldNums")

  def scalaFileName =
    MessageObject.fullName.replace('.', '/') + ".scala"

  def result: CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setName(scalaFileName)
    b.setContent(content)
    b.build()
  }

  def printObject(fp: FunctionalPrinter): FunctionalPrinter =
    fp
      .add(s"object \${MessageObject.name} {")
      .indented(
        _.print(message.getFields().asScala){ (fp, fd) => printField(fp, fd) }
        .add("")
        .print(message.getNestedTypes().asScala) {
          (fp, m) => new MessagePrinter(m, implicits).printObject(fp)
        }
      )
      .add("}")

  def printField(fp: FunctionalPrinter, fd: FieldDescriptor): FunctionalPrinter =
    fp.add(s"val \${fd.getName} = \${fd.getNumber}")

  def content: String = {
    val fp = new FunctionalPrinter()
    .add(
      s"package \${message.getFile.scalaPackage.fullName}",
      "",
    ).call(printObject)
    fp.result()
  }
}
