package edu.holycross.shot.chron


/** Utility class to generate output in various
* formats directly usable by the citemgr system, including
* TTL graphs, and CITE Collections in .[ct]sv format.
*/
class CiteUtils {


    /** First era of Olympia epoch dating.*/
    String olympiadEraString = "14"


    /** Map of fila to rulers for Jerome era 14. */
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


    // Values of these files default to root directory of git project,
    // but can be dynamically redefined.


    /** Source for TEI text of Jerome.*/
    File teiFile = new File("editions/src/Jerome-Chronicles-p5.xml")

    /** Two-column tsv file with source for mapping URNs to appropriate
    * strings for RDF labels. */ 
    File rulerUrnLabelFile = new File("utils/rulerUrnLabelPairs.tsv") 


    /** Two-column tsv file with source for mapping Jerome's header
    * text to URN values for rulers. */
    File rulerHeaderUrnFile = new File("utils/rulerHeaderUrnPairs.tsv")


    CiteUtils() {
    }



    /** Generates TTL output for synchronisms of all years in
    * Olympiad epoch.
    * @returns A String of valid TTL.
    */
    String ttlOlympicSynchronisms() {
        StringBuffer ttlOut = new StringBuffer()

        Jerome j = new Jerome(teiFile)
        j.loadRulerIdMap(rulerHeaderUrnFile)
        j.loadRdfLabelsMap(rulerUrnLabelFile)

        ttlOut.append("\n@prefix chron:        <http://shot.holycross.edu/chron/rdf/> .\n\n")

        def era = j.getEra(olympiadEraString, seedRulers)
        assert era instanceof Era
        era.rulerNameMap = j.headerToUrnMap
        String eraId = era.getEraId()
        while (eraId != "") {
            def eraYears = era.synchronizeYears()
            ttlOut.append( j.synchronsToTtl(eraYears))
            
            def seedNext = era.savedRulerMap
            eraId = j.getNextEraId(eraId)

            if (eraId != "") {
                era = j.getEra(eraId, seedNext)
                era.rulerNameMap = j.headerToUrnMap
            }
        }
        return ttlOut.toString()
    }


}
