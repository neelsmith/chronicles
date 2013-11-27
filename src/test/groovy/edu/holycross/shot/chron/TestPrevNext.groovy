package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestPrevNext extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"



    void testFromCollection() {
        String first = "urn:cite:chron:heraclian.1"
        String ruler = "urn:cite:chron:heraclian.2"
        String nextFull = "urn:cite:chron:heraclian.3"
        JGraph jg = new JGraph(sparql)
        def fullPN =  jg.getPrevNext(ruler)
        assert fullPN[0] == first
        assert fullPN[1] == nextFull

        def firstPN = jg.getPrevNext(first)
        assert firstPN[0] == ""
     }


    void testOlympiad() {
        JGraph jg = new JGraph(sparql)
        String olympiad = "urn:cite:chron:olympiadyear.24_1"
        String expectedPrev = "urn:cite:chron:olympiadyear.92"
        String expectedNext = "urn:cite:chron:olympiadyear.94"

        def olyPN =  jg.getPrevNext(olympiad)
        assert olyPN[0] == expectedPrev
        assert olyPN[1] == expectedNext
    }


}
