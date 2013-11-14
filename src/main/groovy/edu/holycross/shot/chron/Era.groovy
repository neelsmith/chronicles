package edu.holycross.shot.chron

class Era {
    



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


    /** Creates a list of synchronisms.*/
    java.util.ArrayList synchronizeYears() {
        java.util.ArrayList synchronisms = []
        def currentRulerCount = [:]
        columnTypes.eachWithIndex { c, i ->
            if (c == "filum") {
                currentRulerCount[i] = 0
            }
        }
        eraNode[tei.table][tei.row].each { r ->
            // If a regnal row, bump any ruler counts 
            // as needed
            if (r.'@role' == 'regnal') {
                r[tei.cell].eachWithIndex { c, idx ->
                    String colType = columnTypes[idx]
                    switch (colType) {
                        case "filum":
                            if (c.text().size() > 1) {
                            currentRulerCount[idx] =  currentRulerCount[idx] + 1
                        }
                        break
                    }
                }
                
                // if a data row, collect data:
            } else  if (r.'@role' == "data") {
                def year = []
                year.add(["urn:cite:chron:olympiadyear","${r.'@n'}"])
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
                            System.err.println "Era: failed to convert '" + c.text() + "' to int:  ${e}"
                        }
                        String ruler = null
                        
                        def count = currentRulerCount[idx]
                        def filum = filumMap[idx]
                        String label
                        if (fila[filum]) {
                            if (fila[filum][count]) {
                                label = "${fila[filum][count]}".replaceAll(/[ \t\n]+/, " ")
                            } else {
                                System.err.println "NO record ${count} in filum ${fila[filum]}"
                            }
                        } else {
                            System.err.println "NO FILUM '" + filum + "'"
                        }
                        if (c.'@n') {
                            ruler = c.'@n'
                        } else if ( rulerNameMap[label]) {
                            ruler = rulerNameMap[label]
                        }
                        if (ruler != null) {
                            year.add([ruler, yr])
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
                System.err.println "empty filum list in era ${getEraId()}"
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
