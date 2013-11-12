package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestEra extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"



    void testGetEra() {
        Jerome j = new Jerome(new File(teiFile))
        def era = j.getEra("37", ["romans" : ["Augustus"]])
        assert era instanceof Era

        def expectedColumns = ["abraham", "olympiad", "filum", "spatium", "julian"]
        def expectedFilaMap = [2 : "romans"]

        assert era.columnTypes == expectedColumns
        assert era.filumMap == expectedFilaMap

        def realFilaKeys = era.fila.keySet()
        assert realFilaKeys.size() == 1
        realFilaKeys.each { k ->
            assert k == "romans"
        }

        def expectedRulers  = 26     
        assert era.fila["romans"].size() == expectedRulers

        def expectedFirst = "Augustus"
        assert  era.fila["romans"][0] == expectedFirst
        
    }

}
