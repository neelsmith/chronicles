package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestRdfMap extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"
    String mapFile = "collections/rdfRulers.tsv"


    void testMapping() {
        Jerome j = new Jerome(new File(teiFile))
        j.loadRdfLabelsMap(new File(mapFile))
        System.err.println j.rulersToRdfLabels
    }

}
