package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

class Jerome {
    

    /** Groovy xml namespace for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    /** Root node of parsed TEI edition of Jerome.*/
    groovy.util.Node root

    LinkedHashMap headerToUrnMap = [:]

    LinkedHashMap rulersToRdfLabels = [:]

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

    /** Compiles an ordered list of rulers for a given filum.
    * The filum is identified by one of the String values 
    * returned by the getFilaNames method.
    * @param filum String identifying the desired filum.
    * @returns A (possibly empty) ordered list.
    */
    ArrayList getRulerList (String filum) {
        ArrayList rulers = []
        root[tei.text][tei.body][tei.div].each { div ->
            Era era = getEra(div.'@n',[:])
            if (era.fila.keySet().contains(filum)) {
                rulers = rulers + era.fila[filum]                
            }
        }
        return rulers
    }

    // two tab-sep ed columns
    void loadRulerIdMap(File f) {
        f.eachLine {
            def cols = it.split(/\t/)
            headerToUrnMap[cols[0]] = cols[1]            
        }
    }

    void loadRdfLabelsMap(File f) {
        f.eachLine { l ->
            def cols = l.split(/\t/)
            if (cols.size() == 2) {
                rulersToRdfLabels[cols[0]] = cols[1]            
            } else {
                System.err.println "Couldn't form cols from " + l
            }
        }
    }


    String synchronsToTtl(ArrayList synchrons) {
        StringBuffer ttl = new StringBuffer()
        synchrons.each { synList ->

            def synchronizedYears = []
            synList.each { s ->
                CiteUrn seq
                CiteUrn  yr
                Integer epochCount 
                String label
                try  {
                    seq = new CiteUrn(s[0])
                    if (seq.getCollection() == "olympiadyear") {
                        String olyYear = s[1].replace(/./,"_")
                        def parts = olyYear.split("_")
                        Integer olympiad = parts[0].toInteger()
                        Integer subYear = parts[1].toInteger()
                        epochCount = (olympiad - 1) * 4 + subYear
                        yr = new CiteUrn("${seq}.${olyYear}")
                        label = "Olympiad ${olympiad}, year ${subYear}"

                    } else {
                        if (s[1] != null) {
                            yr = new CiteUrn("${seq}.${s[1]}")
                            epochCount = s[1].toInteger()
                        } else {
                            System.err.println "NULL s1 in pair " + s
                        }

                        if (rulersToRdfLabels[seq.toString()]) {
                            label = "Year ${s[1]} of ${rulersToRdfLabels[seq.toString()]}"
                        } else {
                            label = "NO MAPPING for ${seq} in ${rulersToRdfLabels.keySet()}"
                        }
                    }

                    synchronizedYears.add(yr)


                } catch (Exception e) {
                    System.err.println "Jerome:synchronsToTtl: unable to make URN pair from ${s}"
                }

                if ((yr != null) && (seq != null)) { 
                    ttl.append("\n")
                    ttl.append( "<${yr}> olo:item ${epochCount} .\n")
                    ttl.append("<${yr}> rdf:Label " + '"' + label + '" .\n')
                    ttl.append("<${yr}> cite:memberOf <${seq}> .\n")
                }

            }
            Integer max = synchronizedYears.size()  - 1
            synchronizedYears.eachWithIndex { y, i ->
                if ((y != null) && (i < max) ) {
                    for (nxt in (i+1)..(max)) {
                        ttl.append( "<${y}> chron:synchronizedWith <${synchronizedYears[nxt]}> . \n")
                    }
                }
            }
        }
        return ttl.toString()
    }


}
