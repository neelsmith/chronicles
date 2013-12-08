package edu.holycross.shot.chron

/** Class modelling an era in this project's TEI XML edition of the
* Chronicles of Jerome.
*/
class Era {
    

    Integer debug = 0

    /** Groovy xml namespace for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    /** Root node of a table with one era of data */
    groovy.util.Node eraNode

    /** List of column types, in document order.
    * Order is significant because column number is used as
    * index to the following maps! */
    ArrayList columnTypes = []

    /* Map of column indices to filum identifier.
    */
    LinkedHashMap filumMap = [:]

    /** Map of column indices to current value for
    * filum stored in that column.
    */
    LinkedHashMap currentRulerMap = [:]

    /* Map of lists of fila keyed by index
    * to column */
    LinkedHashMap fila = [:]



    LinkedHashMap savedRulerMap = [:]


    /** A map of ruler names, as they figure in Jerome's headers, to URNs. */
    LinkedHashMap rulerNameMap = [:]


    /** Initialize values for ruler in fila map.
    * 
    */
    void initRulers() {
        eraNode[tei.table][tei.row].each { r ->
            switch (r.'@role') {
                // collect ruler names from rows with role 'regnal'
                case "regnal":
                    r[tei.cell].eachWithIndex { c, idx ->
                    if ((columnTypes[idx] == 'filum') && (c.text().size() > 1)) {
                        String msg = "${c.text()}"
                        String fil = filumMap[idx]
                        if (! fila[fil]) {
                            def filumList = [msg]
                            fila[fil] = filumList
                        } else {
                            def filList = fila[fil]
                            filList.add(msg)
                            fila[fil] = filList
                        }
                    }
                }
                break
            }
        }
    }


    /** Constructor initializes all mappings
    * needed to work with this table. 
    * @param Root node of the parsed text for
    * the table of this era.
    * @param rulerMap Map of fila names to an initial list of
    * rulers.
    */
    Era(groovy.util.Node era, LinkedHashMap rulerMap) {
        eraNode = era
        fila = rulerMap
        if (debug > 0) {
            System.err.println "Era constructed with initial ruler map ${fila}"
        }
        eraNode[tei.table][tei.row].each { r ->
            switch (r.'@role') {
                // read column roles from header @role attribute 
                // of header row
                case "header":
                    r[tei.cell].eachWithIndex { c, idx ->
                    columnTypes.add(c.'@role')
                    if (c.'@role' == null)  {
                        System.err.println "##Era ${eraNode.'@n'}: null role in col ${idx}!##"
                    }
                    if (c.'@role' == "filum") {
                        filumMap[idx] = c.'@n'
                    }
                }


                default : 
                    break
            }
        }

        // before adding rulers, put a placeholder in for
        // all fila that are not carried over from previous list
        // NS
        initRulers()
    }


    String tidyString (String s) {
        String str = s.replaceAll(/[\n\s\t]+/,' ')
        str = str.replaceAll('\\*','')
        str = str.replaceFirst(/^ /,'')
        str = str.replaceFirst(/ $/,'')
        return str
    }

    
    java.util.ArrayList indexEvents() {
        ArrayList evts = []
        eraNode[tei.table].each { tab ->
            tab[tei.row].each { r ->
                r[tei.cell].eachWithIndex { col, idx ->
                    String colType = columnTypes[idx]
                    switch (colType) {
                        case "spatium":            
                            if (((r.'@role' == 'data') || (r.'@role' == "olympiad") ) && (col.text() != "")){
                            String txt
                            String noteId = ""
                            col.breadthFirst().each { c ->
                                if (c.getClass().getName() == "java.lang.String") {
                                    def evt = ["urn:cite:chron:jevent.${this.getEraId()}_${tab.'@n'}_${r.'@n'}_${noteId}",  tidyString(c), "urn:cts:latinLit:stoa0162.stoa005:${this.getEraId()}.${tab.'@n'}.${r.'@n'}"]
                                    evts.add(evt)


                                } else if (c.name() instanceof groovy.xml.QName) {
                                    switch(c.name().getLocalPart()) {
                                        case "seg":
                                            def evt = ["urn:cite:chron:jevent.${this.getEraId()}_${tab.'@n'}_${r.'@n'}_${noteId}",  tidyString(c.text()), "urn:cts:latinLit:stoa0162.stoa005:${this.getEraId()}.${tab.'@n'}.${r.'@n'}"]
                                            evts.add(evt)
                                        break
                                        case "note":
                                            noteId = c.'@n'
                                        break

                                    }
                                } else {
                                    System.err.pritln "c is a  " + c.getClass()
                                }
                            }
                        }
                        break
                    }
                }
            }
        }
        return evts
    }

    /** Creates a list of synchronisms.*/
    java.util.ArrayList synchronizeYears() {
        java.util.ArrayList synchronisms = []
        def currentRulerCount = [:]
        columnTypes.eachWithIndex { c, i ->
            if (c == "filum") {
                currentRulerCount[i] = 0
            }
        }

        boolean dataSeen = false
        eraNode[tei.table][tei.row].each { r ->
            // If a regnal row, bump any ruler counts 
            // as needed
            if (r.'@role' == 'regnal') {
                r[tei.cell].eachWithIndex { c, idx ->
                    String colType = columnTypes[idx]
                    switch (colType) {
                        case "filum":
                            if (c.text().size() > 1) {
                            if (dataSeen) {
                                currentRulerCount[idx] =  currentRulerCount[idx] + 1
                            }
                        }
                        break
                    }
                }
                
                // if a data row, collect data:
            } else  if (r.'@role' == "data") {
                dataSeen = true
                def year = []
                year.add(["urn:cite:chron:olympiadyear","${r.'@n'}"])
                if (debug > 1) {
                    System.err.println "Added olympiad " + r.'@n'
                }
                r[tei.cell].eachWithIndex { c, idx ->
                    String colType = columnTypes[idx]
                    switch (colType) {
                        case "spatium":
                            case "julian":
                            case "olympiad":
                            // omit
                            break
                        
                        case "abraham":
                            // add these later...
                            break
                    
                        case "filum":
                            Integer yr = null
                        try {
                            yr = c.text().toInteger()

                        } catch (Exception e) {
                            System.err.println "Era: in row ${r.'@n'} failed to convert [0-origin] column ${idx} '" + c.text() + "' to int:  ${e}"
                        }
                        String ruler = null
                        
                        def count = currentRulerCount[idx]
                        def filum = filumMap[idx]
                        String label

                        if (fila[filum]) {
                            if (debug > 2) {
                                System.err.println "Era: work filum ${filum} in ${fila[filum]} at count ${count}"
                            }
                            if (fila[filum][count]) {

                                label = "${fila[filum][count]}".replaceAll(/[ \t\n]+/, " ")
                                if (debug > 2) {
                                    System.err.println "\t ...yielding '" + label + "' (length ${label.size()} of class ${label.getClass()})"
                                }
                            } else {
                                System.err.println "Era: in row ${r.'@n'}, column ${idx}, with filum ${filum} no record ${count}, for  filum ${fila[filum]}"
                            }

                        } else {
                            System.err.println "Era: in row ${r.'@n'}, NO FILUM '" + filum + "' in map of fila ${fila}."

                        }


                        // GET FILUM INDEX ON rulerNameMap
                        if (c.'@n') {
                            ruler = c.'@n'
                        } else if ( rulerNameMap[label]) {
                            if (debug > 1) {
                                System.err.println "Map for this name: ${rulerNameMap[label]}"
                            }
                            rulerNameMap.keySet().sort().each { k ->
                                if (label == k)  {
                                    if (debug > 0) {
                                        System.err.println "FOUND RULER in name map (${k})"
                                        
                                    } 
                                    ruler = rulerNameMap[k]


                                } else {
                                    if (debug > 2) {
                                        System.err.println "\t${k}-> ${rulerNameMap[k]} TO ${label}"
                                    }
                                }
                            }
                        }
                        if (ruler != null) {
                            if (debug > 0) {
                                System.err.println "Adding ${ruler}, ${yr}"
                            }
                            year.add([ruler, yr])
                        } else {
                            if (debug > 0) {
                                System.err.println "Ruler is null for ${label}. :-( (in filum ${filum}, sequence ${fila[filum]})" 

                                fila[filum].each { filMe ->
                                    if (filMe == label) {
                                        rulerNameMap.keySet().sort().each { nameKey ->
                                            if (nameKey == filMe) {
                                                ruler = rulerNameMap[nameKey]
                                            }
                                        }

                                    }
                                    if (debug > 0) {
                                        System.err.println "FOUND RULER in filum ${filum} ${filMe}: so ${ruler}"
                                        System.err.println "Look for #${filMe}# in ${rulerNameMap.keySet().size()} entries:"
                                        rulerNameMap.keySet().sort().each {
                                            println "\t#${it}#"
                                        }

                                        
                                    }
                                }
                            }
                            if (debug > 3) {
                                rulerNameMap.keySet().sort().each {
                                    System.err.println "\t" + it
                                }
                            }
                        }

                        break
                    }
                }
                synchronisms.add(year)
            }
        }


        // Save map of last ruler to each filum so we can initialize
        // a subsequent era easily
        def saveRulers = [:]
        filumMap.entrySet().each { ent ->
            // get last entry for each
            def filum = fila[ent.value]
            def rulerList = []
            if ((filum) && (filum.size() > 0)) {
                rulerList.add( filum[filum.size() - 1])
            } else {
                System.err.println "Era: in era ${getEraId()}, filum map entry ${ent} has empty filum list."
            }
            saveRulers[ent.value] = rulerList
        }
        savedRulerMap = saveRulers
        //return tableBuffer.toString()

        return synchronisms as java.util.ArrayList
    }


    /** Gets ID string for the Era.
    * @returns ID string.
    */
    String getEraId() {
        return eraNode.'@n'
    }

}
