package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestGraphFilum extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"


    void testRulersForFilum() {
        JGraph jg = new JGraph(sparql)
        String filum = "urn:cite:chron:j-alexandrians"
        Integer expectedSize = 11
        assert jg.getRulersForFilum(filum).size() == expectedSize
    }

}
