import scala.io.Source
import edu.holycross.shot.scm._
import java.io.File



val propertiesFile = "repository.properties"
val cex = LocalFileConverter.textCexFromPropertiesFile(propertiesFile, "#")
// println(cex)
