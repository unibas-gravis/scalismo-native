// this is the implementation build.sbt.
// most settings are inherited either from the top-level build.sbt, or the top-level project/Build.scala

resolvers += "Statismo (public)" at "http://statismo.cs.unibas.ch/repository/public"

libraryDependencies += "ch.unibas.cs.gravis" % "scalismo-native-stub" % productVersion

publishFixup <<= (baseDirectory, name, fixupExclude) map { (base, productName, exclude) =>
  val topDir = s"$publishPrefix/${productPackage.mkString("/")}"
  val dir = s"$topDir/${productName}"
  val excludes = if (exclude.isEmpty) "" else exclude.get.split("\\s+").map { s => s"**/$s/**"}.mkString(" ")
  """ant -file %s/fixup.xml -Djarfile=%s -Dsrcfile=%s -Dproductname=%s -Dfixupexcludes="%s"""".format(
    base,
    s"$dir/$productVersion/${productName}-$productVersion.jar",
    s"$dir/$productVersion/${productName}-$productVersion-sources.jar",
    productName,
    excludes
  ).!

}

publishLocalFixup <<= (baseDirectory, name, fixupExclude) map { (base, productName, exclude) =>
  val excludes = if (exclude.isEmpty) "" else exclude.get.split("\\s+").map { s => s"**/$s/**"}.mkString(" ")
  val dir = s"$publishLocalPrefix/${productPackage.mkString(".")}/${productName}/$productVersion"
  """ant -file %s/fixup.xml -Djarfile=%s -Dsrcfile=%s -Dproductname=%s -Dfixupexcludes="%s"""".format(
    base,
    s"$dir/jars/${productName}.jar",
    s"$dir/srcs/${productName}-sources.jar",
    productName,
    excludes
  ).!
}

publishLocalFixup <<= publishLocalFixup dependsOn(publishLocalFixup in stub, publishLocal)

publishFixup <<= publishFixup dependsOn(publishFixup in stub, publish)

// that's not strictly required for sbt proper, but it helps IntelliJ Idea.
unmanagedBase := (unmanagedBase in stub).value

publishArtifact in(Compile, packageDoc) := true

