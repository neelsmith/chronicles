package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestJGraph extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"

    void testInit() {
        JGraph jg = new JGraph(sparql)
        assert jg
    }    


    void testFila() {
        JGraph jg = new JGraph(sparql)
        Integer expectedSize = 15
        assert jg.getFila().keySet().size() == expectedSize
    }

}
