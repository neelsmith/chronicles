import scala.io.Source
import edu.holycross.shot.scm._

val cexData = Source.fromFile("chronicles.cex").getLines.mkString("\n")
try {
  val citeLib = CiteLibrary(cexData,"#")

  println(s"""\n\n\nCEX validates.""")
  println("Texts validated: " +  citeLib.textRepository.get.catalog.size)
  for (t <- citeLib.textRepository.get.catalog.texts) {
    println(" -- " + t)
  }
} catch {
  case exc : Throwable => println("Validation failed:  " + exc.getMessage() + "\n\n")
}
