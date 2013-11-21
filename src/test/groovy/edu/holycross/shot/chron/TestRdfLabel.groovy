package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestRdfLabel extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"
    String urnStr = "urn:cite:chron:j-alexandrians_ruler12"

    String expected = "Cleopatra"

    void testRdfLabel() {
        JGraph jg = new JGraph(sparql)
        String label =  jg.getRdfLabel(urnStr)
        assert label == expected
    }

}
