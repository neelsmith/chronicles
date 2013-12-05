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
    void addLine(Synchronism sync) {
        if (!nodeList.contains(sync.src)) {
            nodeList.add(sync.src)
            
        }
        if (!nodeList.contains(sync.target)) {
            nodeList.add(sync.target)
        }

        if (!edgeMap.keySet().contains(sync.src)) {
            ArrayList pair = [sync.target, sync]
            edgeMap[sync.src] = [pair]
        } else {
            String target = sync.target
            ArrayList pair = [sync.target, sync]

            if (! edgeMap[sync.src].contains(pair)) {
                def tempMap = edgeMap[sync.src]
                tempMap.add(pair)
                edgeMap[sync.src] = tempMap
            } 
        }

    }



    void walkGraph(String nodeUrn, Integer level) {
        if (nodeUrn == exitUrn) {
            System.err.println "DONE: found ${exitUrn}"
        } else {
            for (i in 0..level) {
                System.err.print "\t"
            }

            System.err.println "${nodeUrn}" 

            level++;            
            edgeMap[nodeUrn].each { child ->
                for (i in 0..level) {
                    System.err.print "\t"
                }
                System.err.println "Edge weight " + child[1].weight()
                walkGraph(child[0], level)
            }
        }
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
                addLine(new Synchronism(cols))
                break
                case max:
                    exitUrn = cols[4]
                addLine(new Synchronism(cols))
                break
                default:
                    addLine(new Synchronism(cols))
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
