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


    /** Finds in the parsed document the root node of 
    * a specified era.
    * @param id ID value of the era.
    * @returns A parsed groovy.util.Node, or null if
    * no matching node is found.
    */
    groovy.util.Node getEraNode(String id) {
        groovy.util.Node era = null
        root[tei.text][tei.body][tei.div].each { div ->
            if (id == div.'@n') {
                 era = div
            }
        }
        return era
    }


    /** Collects a set of all filum names in the MS.
    * @returns A Set of Strings identifying fila.
    */
    Set getFilaNames() {
        def filaList = []
        root[tei.text][tei.body][tei.div].each { div ->
            Era era = getEra(div.'@n',[:])
            filaList =  filaList + era.filumMap.values()
        }
        def filaSet = filaList as Set
        return filaSet
    }



    /** Creates an Era object for the era identified by id,
    * with initial rulers for each filum set to the values
    * mapped in initialRulers.
    * @param id Id for the desired era.
    * @param initialRulers Map of filum names to a (possibly
    * empty) list of ruler names.
    * @returns The representation as an Era object of the 
    * requested era in Jerome's chronology.
    */
    Era getEra(String id, LinkedHashMap initialRulers) {
        groovy.util.Node eraRoot = getEraNode(id)
        return new Era(eraRoot, initialRulers)
    }



    /** Finds the id value for the era following
    * the specified era.
    * @param id Id of the entry after which we want
    * find the next Id value.
    * @returns A String id value, or an empty String
    * if no subsequent era is found.
    */
    String getNextEraId(String id) {
        def foundAt
        String nextId = ""
        root[tei.text][tei.body][tei.div].eachWithIndex { div, count ->
            if (id == div.'@n') {
                foundAt = count
            }
        }
        root[tei.text][tei.body][tei.div].eachWithIndex { div, count ->
            if (count == foundAt + 1) {
                nextId = div.'@n'
            }
        }
        return nextId
    }

}
