package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestPhidias extends GroovyTestCase {


    String csvFile = "testdata/phidias.csv"

    String graphUrl = "http://localhost:3030/ds/"   
    
    void testGraph() {
        SynchGraph sg = new SynchGraph(new File(csvFile), new JGraph(graphUrl))
        sg.walkGraph(sg.entryUrn, 0, 0)
        System.err.println "NODES: " + sg.nodeList
        sg.edgeMap.keySet().each { k  ->
            System.err.println k
            sg.edgeMap[k].each {
                System.err.println "\t" + it[0]
            }

        }
        

    }

}
