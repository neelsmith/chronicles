package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestAllEvents extends GroovyTestCase {


    File teiFile = new File("editions/src/Jerome-Chronicles-p5.xml")


    //File rdfMapFile = new File("utils/rulerUrnLabelPairs.tsv")
    //File nameMapFile  = new File("utils/rulerHeaderUrnPairs.tsv")

    File tsvOut = new File ("evts/jerome-events.tsv")

    String eraString = "14"

    def seedRulers = [
"juda" : ["Hebraeorum Juda XII, AZARIAS, qui et Ozias, annis LII."], 
"egyptians" : ["Dynastia XXIV, BOCCHORUS, annis XLIV."], 
"medes" : ["Medorum II, SOSARMUS, annis XXX."] ,
"macedonians" : ["Macedonum II, COENUS, annis XII."], 
"latins" : ["Latinorum XV, AMULIUS SILVIUS, annis XLIV."],
"israel" : ["Israel, PHACEE, annis XX."], 
 "athenians" : ["Atheniensium XII, AESCHYLUS, annis XXIII."], 
"lydians" : ["Lydorum primus rex, ARDISUS filius Alyattis, annis XXXVI."] 
    ]


    Integer cutOff = 0

    void testSequence() {
        Jerome j = new Jerome(teiFile)
        tsvOut.append("Event\tJText\tPsg\n")

        def era = j.getEra(eraString, seedRulers)
        assert era instanceof Era

        def evts = []

        String eraId = era.getEraId()
        while (eraId != "") {
            def eraEvts = era.indexEvents()
            evts = evts + eraEvts
            eraEvts.each { triple ->
                tsvOut.append("${triple[0]}\t${triple[1]}\t${triple[2]}\n")
            }
            System.err.println "${eraId}: ${eraEvts.size()} events, total now ${evts.size()}"
            def seedNext = era.savedRulerMap
            eraId = j.getNextEraId(eraId)

            System.err.println "Seeding ${eraId} with ${seedNext}"
            if (eraId != "") {
                era = j.getEra(eraId, seedNext)

            }
        }

    }

}
