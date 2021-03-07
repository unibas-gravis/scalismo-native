// this is the stub build.sbt.
// most settings are inherited either from the top-level build.sbt, or the top-level project/Build.scala

name := "scalismo-native-stub"

publishArtifact in(Compile, packageDoc) := true

publishFixup <<= (baseDirectory, unmanagedBase in Compile, name) map { (base, lib, productName) =>
  val topDir = s"$publishPrefix/${productPackage.mkString("/")}"
  val dir = s"$topDir/${productName}"
  """ant -file %s/fixup.xml -Djarfile=%s -Dsrcfile=%s -Dlibdir=%s -Dproductname=%s""".format(
    base,
    s"$dir/$productVersion/${productName}-$productVersion.jar",
    s"$dir/$productVersion/${productName}-$productVersion-sources.jar",
    lib,
    productName
  ).!
}

publishLocalFixup <<= (baseDirectory, unmanagedBase in Compile, name) map { (base, lib, productName) =>
  val dir = s"$publishLocalPrefix/${productPackage.mkString(".")}/${productName}/$productVersion"
  """ant -file %s/fixup.xml -Djarfile=%s -Dsrcfile=%s -Dlibdir=%s -Dproductname=%s""".format(
    base,
    s"$dir/jars/${productName}.jar",
    s"$dir/srcs/${productName}-sources.jar",
    lib,
    productName
  ).!
}

publishLocalFixup <<= publishLocalFixup dependsOn publishLocal

publishFixup <<= publishFixup dependsOn publish
