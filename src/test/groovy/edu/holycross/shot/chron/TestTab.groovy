package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestTab extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"

    String eraString = "35"
    def seedRulers = ["romans" : ["Augustus"], "judaeans" : ["Herodes"], "alexandrians" : ["Dunno"]]


    void testTabulation() {
        Jerome j = new Jerome(new File(teiFile))
        def era = j.getEra(eraString, seedRulers)
        assert era instanceof Era
        

        System.err.println "Tabulation of ${eraString}"
        System.err.println era.tabulate()
        
    }

}
