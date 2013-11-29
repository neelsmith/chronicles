package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test

class TestEgyptian extends GroovyTestCase {


    void testValidConstructors() {
        EgyptianDate ed = new EgyptianDate("thoth", 21)
        EgyptianDate ed2 = new EgyptianDate(EgyptianMonth.THOTH, 21)
        EgyptianDate ed3 = new EgyptianDate("Thoth", 21)

        assert ed.day == 21
        assert ed2.day == 21
        assert ed3.day == 21
        assert ed.month == EgyptianMonth.THOTH
        assert ed2.month == EgyptianMonth.THOTH
        assert ed3.month == EgyptianMonth.THOTH

    }

    void testBadConstructors() {
        // day out of range
        shouldFail {
            EgyptianDate ed = new EgyptianDate("thoth", 31)
        }
        

        // bad month name
        shouldFail {
            EgyptianDate ed = new EgyptianDate("January", 21)
        }
    }

}
