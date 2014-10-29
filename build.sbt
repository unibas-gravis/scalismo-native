import AssemblyKeys._

// this must appear as a standalone line before any other instructions related
// to https://github.com/sbt/sbt-assembly
assemblySettings

val productPackage = Seq("org","statismo")
val productName = "nativelibs"
val productVersion = "1.4.0"

val scalaMinorVersion = "2.10"
val scalaReleaseVersion = "4"

val publishPrefix = "/export/contrib/statismo/repo/public"
val publishLocalPrefix = s"${System.getProperty("user.home")}/.ivy2/local"


organization := productPackage.mkString(".")

name := productName

version := productVersion

scalaVersion := s"$scalaMinorVersion.$scalaReleaseVersion"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint", "-deprecation", "-unchecked", "-feature", "-target:jvm-1.6")

EclipseKeys.withSource := true

publishTo := Some(Resolver.file("file",  new File( publishPrefix )) )

TaskKey[Unit]("publish-fixup") <<= (unmanagedBase in Compile) map { lib =>
  val topDir = s"$publishPrefix/${productPackage.mkString("/")}"
  val dir = s"$topDir/${productName}_$scalaMinorVersion"
  """ant -Djarfile=%s -Dsrcfile=%s -Dlibdir=%s""".format(
    s"$dir/$productVersion/${productName}_$scalaMinorVersion-$productVersion.jar",
    s"$dir/$productVersion/${productName}_$scalaMinorVersion-$productVersion-sources.jar",
    lib
  ).!
}

TaskKey[Unit]("publish-local-fixup") <<= (unmanagedBase in Compile) map {  lib =>
  val dir = s"$publishLocalPrefix/${productPackage.mkString(".")}/${productName}_$scalaMinorVersion/$productVersion"
    """ant -Djarfile=%s -Dsrcfile=%s -Dlibdir=%s""".format(
      s"$dir/jars/${productName}_$scalaMinorVersion.jar",
      s"$dir/srcs/${productName}_$scalaMinorVersion-sources.jar",
      lib
    ).!
}

{
	println()
  println("===============================================================================")
  println("To publish this project, run \"publish\", then \"publish-fixup\"!")
  println("To publish-local this project, run \"publish-local\", then \"publish-local-fixup\"!")
	println("===============================================================================")
	println()
	publishArtifact in (Compile, packageDoc) := false
}
