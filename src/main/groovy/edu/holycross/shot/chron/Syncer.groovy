package edu.holycross.shot.chron


/** Utility class to generate synchronizations for all
* of Jerome in the Olympiad epch as TTL. 
*/
class Syncer {


    String eraString = "14"

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


    File teiFile = new File("editions/src/Jerome-Chronicles-p5.xml")
    File rdfMapFile = new File("collections/rdfRulers.tsv")
    File nameMapFile  = new File("collections/rulerIdMap.tsv")


    File ttlOut 

    Syncer(File outFile) {
        ttlOut = outFile
    }


    void sync() {
        Jerome j = new Jerome(teiFile)
        j.loadRulerIdMap(nameMapFile)
        j.loadRdfLabelsMap(rdfMapFile)

        ttlOut.append("\n@prefix chron:        <http://shot.holycross.edu/chron/rdf/> .\n\n")

        def era = j.getEra(eraString, seedRulers)
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
    }



    public static void main (String[] args) throws Exception {
        String outFileName = args[0]
        try {
            File f = new File(outFileName)
            Syncer syncer = new Syncer(f)
            syncer.sync()

        } catch (Exception e) {
            throw e
        }
    }
}
