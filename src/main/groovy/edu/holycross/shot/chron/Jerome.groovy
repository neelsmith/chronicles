package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

class Jerome {

    Integer debug = 0

    /** Groovy xml namespace for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    /** Root node of parsed TEI edition of Jerome.*/
    groovy.util.Node root



    /** A map of ruler names, as they figure in Jerome's headers, to URNs. 
    * This is the same structure as (and can be set to a copy of) the Jerome
    * class's rulerNameMap. */
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




    /* Utility methods used in seeding data for analyzing an Era structure.*/

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


    /**  Composes TTL representation of synchronizations
    * that Era generates.
    * @param synchrons A list of year lists, where each year list
    * consists of one or more year records, and each year record
    * consists of a chronology sequence URN and an identifier within
    * the sequence.  For cyclical Olympiad year sequences, the identifier will be of
    * OLYMPIAD.[1-4];  for other numeric sequences, the identifier will be an
    * integer giving a year number (a 1-origin index).
    * @returns A TTL description of synchronizations.
    */
    String synchronsToTtl(ArrayList synchrons) {
        StringBuffer ttl = new StringBuffer()
        synchrons.each { synList ->

            def synchronizedYears = []
            synList.eachWithIndex { s, sIndex ->
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
                            yr = new CiteUrn("${seq}_${s[1]}")
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
                    System.err.println "Jerome:synchronsToTtl: unable to make URN pair from ${s}."
                    
                    System.err.print "(Previous s: "
                    if ((sIndex - 1) >= 0) {
                        System.err.print "${synchronizedYears[sIndex - 1]}"
                    }
                    System.err.print "; next s: "
                    if (synchronizedYears.size() > (sIndex + 1)) {
                        System.err.print "${synchronizedYears[sIndex + 1]}"
                    }
                    System.err.println ".)"
                }


                if ((yr != null) && (seq != null)) { 
                    ttl.append("\n")
                    ttl.append( "<${yr}> olo:item ${epochCount} .\n")
                    ttl.append("<${yr}> rdf:Label " + '"' + label + '" .\n')
                    ttl.append("<${yr}> cite:memberOf <${seq}> .\n")
                }

            }
            Integer max = synchronizedYears.size()  - 1

            if (debug > 0) {          
                System.err.println "Synchr. years: " + synchronizedYears  }
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
