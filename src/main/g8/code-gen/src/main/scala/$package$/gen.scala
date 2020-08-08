package $package$

import protocbridge.Artifact
import scalapb.GeneratorOption
import protocbridge.SandboxedJvmGenerator

object gen {
  def apply(
      options: GeneratorOption*
  ): (SandboxedJvmGenerator, Seq[String]) =
    (
      SandboxedJvmGenerator.forModule(
        "scala",
        Artifact(
          $package$.compiler.BuildInfo.organization,
          "$name;format="hyphen"$-codegen_2.12",
          $package$.compiler.BuildInfo.version
        ),
        "$package$.compiler.CodeGenerator\$",
        $package$.compiler.CodeGenerator.suggestedDependencies
      ),
      options.map(_.toString)
    )

  def apply(
      options: Set[GeneratorOption] = Set.empty
  ): (SandboxedJvmGenerator, Seq[String]) = apply(options.toSeq: _*)
}