package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test

class TestObservation extends GroovyTestCase {

    String sparql = "http://localhost:3030/ds/"
    CiteUrn u = new CiteUrn("urn:cite:chron:pedersen.1")    

    String expectedPlace = "Babylon"
    String expectedRuler = "urn:cite:chron:heraclian.5"
    Integer expectedYear = 721
    Integer expectedYearInReign = 1

    void testConstructors() {
        Observation obs = new Observation(u, sparql)
        assert obs.urn == u
        assert obs.placeName == expectedPlace
        assert obs.ruler.toString() == expectedRuler
        assert obs.yrInReign == expectedYearInReign

        assert obs.gdate.get(Calendar.YEAR) == expectedYear
        assert obs.gdate.get(Calendar.ERA) == GregorianCalendar.BC
    }

}
