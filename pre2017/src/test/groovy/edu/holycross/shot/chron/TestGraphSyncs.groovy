package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestGraphSyncs extends GroovyTestCase {


    String sparql = "http://localhost:3030/ds/"

    String ruler = "urn:cite:chron:j-macedonians_ruler30"

    void testSynchronisms() {
        JGraph jg = new JGraph(sparql)
        def syncs =  jg.getSynchronismsForRuler(ruler)
        System.err.println syncs
    }

}
