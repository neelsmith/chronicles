package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestSeq extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"


    //String eraString = "36"
    //def seedRulers = ["romans" : ["Augustus"], "judaeans" : ["Herodes"]]


    String eraString = "35"
    def seedRulers = ["romans" : ["Romanorum primus, C. JULIUS CAESAR, annis IV, menses VII"], "judaeans" : ["Judaei Romanorum vectigales per Pompeium facti, et pontificatum apud eos suscepit HYRCANUS, annis XXXIV"], "alexandrians" : ["Aegypti XII, CLEOPATRA, annis XXII"]]

    //String eraString = "14"    
    //def seedRulers = ["medes": [], "juda": [], "israel" : [], "athenians" : [] ,"latins" : [], "macedonians" : [], "lydians" : [], "egyptians" : []]



    //String eraString = "18"
    //def seedRulers = ["persians" : [], "captivity" : [], "romans" : [], "macedonians"  : [], "lydians" : [], "egyptians" : []]

    //String eraString = "26"
    //def seedRulers = ["macedonians" : [], "alexandrians" : []]

    void testSequence() {
        Jerome j = new Jerome(new File(teiFile))
        def era = j.getEra(eraString, seedRulers)
        assert era instanceof Era

        String eraId = era.getEraId()
        while (eraId != "") {
            System.err.println "Era " + eraId
            System.err.println era.tabulate()
            def seedNext = era.savedRulerMap
            eraId = j.getNextEraId(eraId)
            if (eraId != "") {
                era = j.getEra(eraId, seedNext)
            }
        }

    }

}
