package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestEra extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"



    void testGetEra() {
        Jerome j = new Jerome(new File(teiFile))
        def era = j.getEra("37")
        assert era instanceof Era

        def expectedColumns = ["abraham", "olympiad", "filum", "spatium", "julian"]
        def expectedFila = [2 : "romans"]

        assert era.columnTypes == expectedColumns
        assert era.filumMap == expectedFila
        
    }

}
