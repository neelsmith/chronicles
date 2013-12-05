package edu.holycross.shot.chron


class SynchGraph {
    
    /** List of vertices in the graph. */
    ArrayList nodeList = []
    

    /** Adjacency map. */
    LinkedHashMap edgeMap = [:]

    /** URN of entry node to graph */
    String entryUrn 

    /** URN of exit node to graph */
    String exitUrn

    //evidence,event1,synchronization,synctype,event2,units
    void addLine(ArrayList columns) {
    }




    void initGraph(String csvString) {
        Integer count = 0
        Integer max = csvString.readLines().size() - 1
        csvString.eachLine { l ->
            // change this to proper csv parse...
            ArrayList cols = l.split(/,/)
            switch (count) {
                case 0:
                    // skip
                    break
                case 1:
                    entryUrn = cols[1]
                addLine(cols)
                break
                case max:
                    exitUrn = cols[4]
                addLine(cols)
                break
                default:
                    addLine(cols)
                break
            }
            count++
        }
    }



    /** Constructor initializing graph from a .csv file.
    * @param f CSV file.
    */  
    SynchGraph(File f) {
        initGraph(f.getText("UTF-8"))
    }
}
