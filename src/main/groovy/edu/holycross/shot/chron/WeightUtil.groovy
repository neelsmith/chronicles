package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn



class WeightUtil {

    public static void main (String[] args) throws Exception {
        String graphFile = args[0]
        String graphUrl = args[1]

        try {
            File f = new File(graphFile)

            System.err.println "Reading synchronism graph from file " + graphFile
            JGraph jg = new JGraph(graphUrl)
            SynchGraph sg = new SynchGraph(f, jg)
            Number wt = sg.weightForPath(sg.entryUrn, sg.exitUrn,0)

            CiteUrn dateUrn
            try {
                dateUrn = new CiteUrn(sg.exitUrn)
            } catch (Exception e) {
                System.err.println "${sg.exitUrn} not a valid URN?"
            }

            System.out.println "\nCHRONOLOGY: Measuring distance from '" + jg.getRdfLabel(sg.entryUrn) + "' to '" + jg.getRdfLabel(sg.exitUrn) + "'"
            System.out.print "CONCLUSION:  '" + jg.getRdfLabel(sg.entryUrn) + "'"
            try {
                if (dateUrn.getCollection() == "pedersen") {
                    if (wt == 0) {
                        System.err.println " dates to exactly ${sg.showDate(dateUrn)}"
                    } else if (wt > 0) {
                        System.err.println " follows ${sg.showDate(dateUrn)} by ${wt} years"
                    } else {
                        System.err.println " precedes ${sg.showDate(dateUrn)} by ${-1 * wt} years"
                    }
                }
            } catch (Exception e) {
                System.err.println "Couldn't show date." + e
            }

        } catch (Exception e) {
            throw e
        }
    }
}
