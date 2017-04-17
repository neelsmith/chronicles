
scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo
resolvers += "beta" at "http://beta.hpcc.uh.edu/nexus/content/repositories/releases"

libraryDependencies +=  "edu.holycross.shot" %% "scm" % "2.1.2"
