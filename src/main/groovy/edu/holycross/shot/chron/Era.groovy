package edu.holycross.shot.chron

class Era {
    

    /** Groovy xml namespace for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    /** Root node of a table with one era of data */
    groovy.util.Node eraTable

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




    /** Initialize values for ruler in fila map.
    */
    void initRulers(LinkedHashMap rulerMap) {
        eraTable[tei.table][tei.row].each { r ->
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
    */
    Era(groovy.util.Node era) {
        eraTable = era

        eraTable[tei.table][tei.row].each { r ->
            switch (r.'@role') {
                // read column roles from header @role attribute 
                // of header row
                case "header":
                    r[tei.cell].eachWithIndex { c, idx ->
                    columnTypes.add(c.'@role')
                    if (c.'@role' == null)  {
                        System.err.println "##Era ${docNode.'@n'}: null role in col ${idx}!##"
                    }
                    if (c.'@role' == "filum") {
                        filumMap[idx] = c.'@n'
                    }
                }


                default : 
                    break
            }
        }
        initRulers()
    }


    /** Creates string with tabular representation of chronology.*/
    String tabulate() {
        StringBuffer tableBuffer = new StringBuffer()
        def currentRulerCount = [:]
        columnTypes.eachWithIndex { c, i ->
            if (c == "filum") {
                currentRulerCount[i] = 0
            }
        }

        eraTable[tei.table][tei.row].each { r ->
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
            } else  if (r.'@n') {
                tableBuffer.append( "olympiadyear: ${r.'@n'}\t")

                r[tei.cell].eachWithIndex { c, idx ->

                    String colType = columnTypes[idx]
                    switch (colType) {
                        
                        case "spatium":
                            case "julian":
                            case "olympiad":
                            // omit
                            break
                        
                        case "abraham":
                            break
                    
                        case "filum":
                            tableBuffer.append("#${idx}: ${c.text()} of ")
                        def count = currentRulerCount[idx]
                        def filum = filumMap[idx]
                        //System.err.println "count is ${currentRulerCount[idx]}"
                        tableBuffer.append("${fila[filum][count]}#\t")
                        break
                    }
                }
                tableBuffer.append("\n")
            }
        }
        return tableBuffer.toString()
    }


}