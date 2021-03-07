resolvers += Resolver.url("bintray-sbt-plugin-releases", url("https://dl.bintray.com/banno/oss"))(
  Resolver.ivyStylePatterns
)

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"


addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.3.0")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
