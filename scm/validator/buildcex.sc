import scala.io.Source
import edu.holycross.shot.scm._
import java.io.File


val header="""
# Library of texts from Holy Cross with important
# sources for chronology.

# The pound sign "#" is used as a column divider.

#!cexversion
1.0

#!citelibrary

name#Holy Cross corpus of texts for ancient chronology
urn#urn:cite2:hcchron:cex.2017a:chronciles
license#Creative Commons Attribution Share Alike

"""


val propertiesFile = "repository.properties"
val cex = LocalFileConverter.textCexFromPropertiesFile(propertiesFile, "#")


import java.io.PrintWriter
new PrintWriter("chronicles.cex"){ write(header + cex); close;}
