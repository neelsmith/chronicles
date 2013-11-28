package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestGraphFilum extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"

    String filum = "urn:cite:chron:j-lydians"
    Integer expectedSize = 9

    void testRulersForFilum() {
        JGraph jg = new JGraph(sparql)
        def filumSeq =  jg.getRulersForFilum(filum)

        //System.err.println "Query to get rulers for filum with : "
        //System.err.println jg.jqg.getRulersForFilumQuery(filum)
        //System.err.println "FILUM IS " + filumSeq
        assert filumSeq.size() == expectedSize
    }

}
