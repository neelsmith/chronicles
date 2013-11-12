package edu.holycross.shot.chron

class Jerome {
    

    /** Groovy xml namespace for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    /** Root node of parsed TEI edition of Jerome.*/
    groovy.util.Node root


    /** Constructor building chronology from the
    * TEI edition in a file.
    * @param f The XML file with edition of Jerome.
    */
    Jerome(File f) {
        root = new XmlParser().parse(f)
    }

    groovy.util.Node getEraNode(String id) {
        groovy.util.Node era = null
        root[tei.text][tei.body][tei.div].each { div ->
            if (id == div.'@n') {
                 era = div
            }
        }
        return era
    }

    Era getEra(String id) {
        groovy.util.Node eraRoot = getEraNode(id)
        return new Era(eraRoot)
    }

}