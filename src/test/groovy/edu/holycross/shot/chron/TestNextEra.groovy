package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestNextEra extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"

    String eraString = "14"
    String expectedNext = "15"
    String lastEra = "39"

    void testSequence() {
        Jerome j = new Jerome(new File(teiFile))
        assert j.getNextEraId(eraString) == expectedNext

        assert j.getNextEraId(lastEra) == ""
    }

}
