# $name$

A Protoc plugin that generates...

# Using the plugin

To add the plugin to another project:

```
addSbtPlugin("com.thesamet" % "sbt-protoc" % "$sbt_protoc_version$")

libraryDependencies += "com.example" %% "$name;format="norm"$-codegen" % "0.1.0"
```

and the following to your `build.sbt`:
```
PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value / "scalapb",
  $package$.gen() -> (sourceManaged in Compile).value / "scalapb"
)
```

# Development and testing

Code structure:
- `core`: contains the runtime library for this plugin
- `code-gen`: contains the protoc plugin (code generator)
- `e2e`: an integration test for the plugin

To test the plugin, within SBT:

```
> e2eJVM2_13/test
```

or 

```
> e2eJVM2_12/test
```

