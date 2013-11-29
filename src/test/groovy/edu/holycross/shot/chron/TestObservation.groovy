package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test

class TestObservation extends GroovyTestCase {

    String sparql = "http://localhost:3030/ds/"
    CiteUrn u = new CiteUrn("urn:cite:chron:pedersen.1")    
    void testConstructors() {
        Observation obs = new Observation(u, sparql)
        assert obs
    }

}
