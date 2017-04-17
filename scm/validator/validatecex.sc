import scala.io.Source
import edu.holycross.shot.scm._
import java.io.File



val cexData = Source.fromFile("chronicles.cex").getLines.mkString("\n")
try {
  val citeLib = CiteLibrary(cexData,"#")
  println(s"""\n\n\nCEX validates.""")
} catch {
  case exc : Throwable => println("Validation failed:  " + exc.getMessage() + "\n\n")
}
