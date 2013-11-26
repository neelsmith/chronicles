package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestAllFila extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"
    Integer expectedSize = 21

    void testSequence() {
        Jerome j = new Jerome(new File(teiFile))
        def filaNames = j.getFilaNames()
        assert filaNames.size() == expectedSize

        filaNames.sort().each {
            println it
        }
    }

}
