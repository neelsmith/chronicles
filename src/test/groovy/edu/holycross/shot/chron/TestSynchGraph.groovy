package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestSynchGraph extends GroovyTestCase {


    String csvFile = "testdata/pelop-war.csv"

   
    
    void testGraphOps() {
        SynchGraph sg = new SynchGraph(new File(csvFile))       
        String expectedEntry = "urn:cite:chron:demo.pelopwar"
        String expectedExit = "urn:cite:chron:pedersen.8"

        assert sg.entryUrn == expectedEntry
        assert sg.exitUrn == expectedExit

    }

}
