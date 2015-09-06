name := "bosch-hbs-sbt-parent"

organization := "com.bosch"

version := "1.7-SNAPSHOT"

sbtPlugin := true

publishMavenStyle := true

publishTo := {
	val nexus = "https://rb-tmp-dev.de.bosch.com/nexus-pro"
	if (isSnapshot.value)
		Some("HBS Snapshots" at nexus + "/content/repositories/hbs-snapshots/")
	else
		Some("HBS Releases" at nexus + "/content/repositories/hbs-releases/")
}

credentials += Credentials("Sonatype Nexus Repository Manager", "rb-tmp-dev.de.bosch.com", "fX9ns3rI", "+92KPCzJhIUqiyMGbWKWqAJlRfpCi1PuzXyFr0bqYC1g")