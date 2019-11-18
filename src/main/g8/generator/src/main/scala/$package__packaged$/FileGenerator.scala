package $package$

import scala.collection.JavaConverters._

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import com.google.protobuf.Descriptors._

import scalapb.compiler.DescriptorImplicits
import scalapb.compiler.FunctionalPrinter


/* Called by Generator and generates an output file for each proto file */
class FileGenerator(implicits: DescriptorImplicits) {
  import implicits._

  def generateFile(fileDesc: FileDescriptor): CodeGeneratorResponse.File = {
    val b = CodeGeneratorResponse.File.newBuilder()
    b.setName(s"\${fileDesc.scalaDirectory}/\${fileDesc.fileDescriptorObjectName}Foo.scala")
    val fp = FunctionalPrinter()
      .add(s"package \${fileDesc.scalaPackageName}")
      .add("")
      .print(fileDesc.getMessageTypes.asScala) {
        case (p, m) =>
          p.add(s"object \${m.getName}Boo {")
            .indent
            .add(s"type T = \${m.scalaTypeName}")
            .add(s"val FieldCount = \${m.getFields.size}")
            .outdent
            .add("}")
      }
      b.setContent(fp.result)
      b.build
  }
}