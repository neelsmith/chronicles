package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestPrevNext extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"


    String first = "urn:cite:chron:heraclian.1"
    String ruler = "urn:cite:chron:heraclian.2"
    String nextFull = "urn:cite:chron:heraclian.3"

    void testSynchronisms() {
        JGraph jg = new JGraph(sparql)
        def fullPN =  jg.getPrevNext(ruler)
        assert fullPN[0] == first
        assert fullPN[1] == nextFull

        def firstPN = jg.getPrevNext(first)
        assert firstPN[0] == ""
     }

}
