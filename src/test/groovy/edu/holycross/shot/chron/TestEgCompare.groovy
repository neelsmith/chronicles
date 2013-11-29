package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test

class TestEgCompare extends GroovyTestCase {


    void testEquality() {
        EgyptianDate ed = new EgyptianDate("thoth", 21)
        EgyptianDate ed2 = new EgyptianDate(EgyptianMonth.THOTH, 21)
        EgyptianDate ed3 = new EgyptianDate("Thoth", 21)

        assert ed == ed2
        assert ed2 == ed3

    }
}
