package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestJerome extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"

    void testInit() {
        Jerome j = new Jerome(new File(teiFile))
        assert j
    }    

}
