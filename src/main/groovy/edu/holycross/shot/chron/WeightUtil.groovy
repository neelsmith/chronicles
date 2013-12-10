package edu.holycross.shot.chron



class WeightUtil {

    public static void main (String[] args) throws Exception {
        String graphFile = args[0]
        try {
            File f = new File(graphFile)
            System.err.println "Reading synchronism graph from file " + graphFile
            SynchGraph sg = new SynchGraph(f)
            Number wt = sg.weightForPath(sg.entryUrn, sg.exitUrn,0)
            System.out.println "Path from " + sg.entryUrn + " to " + sg.exitUrn
            System.out.println "Distance: " + wt

        } catch (Exception e) {
            throw e
        }
    }
}
