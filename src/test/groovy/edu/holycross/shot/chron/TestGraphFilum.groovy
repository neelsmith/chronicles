package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestGraphFilum extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"

    String filum = "urn:cite:chron:j-alexandrians"
    Integer expectedSize = 14

    void testRulersForFilum() {
        JGraph jg = new JGraph(sparql)
        def filumSeq =  jg.getRulersForFilum(filum)
        assert filumSeq.size() == expectedSize
    }

}
