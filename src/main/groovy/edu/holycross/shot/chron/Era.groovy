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



    /** Constructor initializes all mappings
    * needed to work with this table. 
    * @param Root node of the parsed text for
    * the table of this era.
    */
    Era(groovy.util.Node era) {
        eraTable = era

        eraTable[tei.table][tei.row].each { r ->
            switch (r.'@role') {
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

                case "regnal":
                    r[tei.cell].eachWithIndex { c, idx ->
                    if (c.text().size() > 1) {
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

                default : 
                    break
            }
        }
    }
}
