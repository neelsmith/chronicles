package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestLabel extends GroovyTestCase {



    String sparql = "http://localhost:3030/ds/"

    String urn = "urn:cite:chron:j-persians_ruler1"
    String expected = "Cyrus"

    void testGetLabel() {
        JGraph jg = new JGraph(sparql)
        assert jg.getLabel(urn) == expected
    }

}
