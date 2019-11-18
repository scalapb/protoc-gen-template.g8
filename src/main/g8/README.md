# $name$

A Protoc plugin that generates...

To test the plugin, within SBT:

```
> e2e/test
```

# Using the plugin

To add the plugin to another project, you need publish it first on maven, or publish locally by using `+publishLocal`.

In the other project, add the following to `project/plugins.sbt`:

```
addSbtPlugin("com.thesamet" % "sbt-protoc" % "$sbt_protoc$")

libraryDependencies += "$organization$" %% "$name$" % "0.1.0"
```

and the following to your `build.sbt`:
```
PB.targets in Compile := Seq(
  $package$.Generator -> (sourceManaged in Compile).value
)
```