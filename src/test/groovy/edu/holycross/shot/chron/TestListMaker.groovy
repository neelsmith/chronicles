package edu.holycross.shot.chron

import static org.junit.Assert.*
import org.junit.Test

class TestListMaker extends GroovyTestCase {


    String teiFile = "editions/src/Jerome-Chronicles-p5.xml"
    Integer expectedSize = 9
    String croesus = "Lydorum IX, CROESUS, annis XV."

    void testSequence() {
        Jerome j = new Jerome(new File(teiFile))
        def lydians = j.getRulerList("lydians")
        assert lydians.size() ==  expectedSize
        assert lydians[expectedSize - 1] == croesus

    }

}
