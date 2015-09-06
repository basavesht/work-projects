// Comment to get more information during initialization
logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers +=  "Sonatype Nexus Repository Manager" at "https://rb-tmp-dev.de.bosch.com/nexus-pro/content/repositories/hbs-snapshots/"

credentials += Credentials("Sonatype Nexus Repository Manager", "rb-tmp-dev.de.bosch.com", "fX9ns3rI", "+92KPCzJhIUqiyMGbWKWqAJlRfpCi1PuzXyFr0bqYC1g")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

// The Bosch plugin
addSbtPlugin("com.bosch.hbs" % "bosch-hbs-sbt-parent" % "1.7-SNAPSHOT")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")