package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

class SynchGraph {
    
    /** List of vertices in the graph. */
    ArrayList nodeList = []

    /** Adjacency map. */
    LinkedHashMap edgeMap = [:]

    /** URN of entry node to graph */
    String entryUrn 

    /** URN of exit node to graph */
    String exitUrn

    JGraph jGraph = null


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

    String showDate(CiteUrn urn ) {
        Observation observation = new Observation(urn, jGraph.tripletServerUrl)
        return "${observation.gdate.get(Calendar.YEAR)} ${observation.gdate.get(Calendar.ERA)}"
    }

    void walkGraph(String nodeUrn, Integer level, Number cumulativeWeight) {
        if (nodeUrn == exitUrn) {
            System.err.println "DONE: found ${exitUrn}"
            CiteUrn dateUrn
            try {
                dateUrn = new CiteUrn(exitUrn)
            } catch (Exception e) {
                System.err.println "${exitUrn} not a valid URN?"
            }
            try {
                if ((dateUrn.getCollection() == "pedersen") && (jGraph != null)) {
                    if (cumulativeWeight == 0) {
                        System.err.println "Exact date = ${showDate(dateUrn)}"
                    } else if (cumulativeWeight > 0) {
                        System.err.println "Event follows ${showDate(dateUrn)} by ${cumulativeWeight} units"
                    } else {
                        System.err.println "Event precedes ${showDate(dateUrn)} by ${cumulativeWeight} units"
                    }
                }
            } catch (Exception e) {
                System.err.println "Couldn't show date." + e
            }
                

        } else {
            for (i in 0..level) {
                System.err.print "\t"
            }
            System.err.print "${level}. Source urn "
            System.err.println "${nodeUrn}" 

            level++;            
            edgeMap[nodeUrn].each { child ->
                Number newWeight = cumulativeWeight + child[1].weight()
                for (i in 0..level) {
                    System.err.print "\t"
                }

                System.err.println "Edge weight " + child[1].weight() + " from " + child[1] + " so  new TOTAL " + newWeight
                
                walkGraph(child[0], level, newWeight)
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
                addLine(new Synchronism(cols, jGraph))
                break
                case max:
                    exitUrn = cols[4]
                addLine(new Synchronism(cols, jGraph))
                break
                default:
                    addLine(new Synchronism(cols, jGraph))
                break
            }
            count++
        }
    }

    SynchGraph(File f, JGraph jg) {
        this.jGraph = jg
        initGraph(f.getText("UTF-8"))
    }

    /** Constructor initializing graph from a .csv file.
    * @param f CSV file.
    */  
    SynchGraph(File f) {
        initGraph(f.getText("UTF-8"))
    }
}
