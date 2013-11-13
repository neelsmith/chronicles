package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestAllFila extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"

    void testSequence() {
        Jerome j = new Jerome(new File(teiFile))
        System.err.println j.getFilaNames()
    }

}
