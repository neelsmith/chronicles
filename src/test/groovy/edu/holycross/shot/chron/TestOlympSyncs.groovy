package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestOlympSyncs extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"

    String olympiad = "urn:cite:chron:olympiadyear.24_1"

    Integer expectedSize = 5
    void testSynchronisms() {
        JGraph jg = new JGraph(sparql)
        def syncs =  jg.getSyncsForOlympiadYear(olympiad)
        assert syncs.size() == expectedSize
    }

}
