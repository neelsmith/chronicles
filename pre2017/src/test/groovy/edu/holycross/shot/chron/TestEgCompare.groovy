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

    void testMonthOrder() {
        assert (EgyptianMonth.EPAGOMENAL  > EgyptianMonth.THOTH)
        assert EgyptianMonth.EPAGOMENAL.ordinal() == 12
    }

    void testDayCount() {
        EgyptianDate day1 = new EgyptianDate("thoth", 1)
        assert day1.dayInYear() == 1
        
        EgyptianDate lastday = new EgyptianDate("epagomenal", 5)
        assert lastday.dayInYear() == 365
    }

}
