package edu.holycross.shot.chron



class Syncer {

    public static void main (String[] args) throws Exception {
        String outFileName = args[0]
        try {
            File f = new File(outFileName)
            CiteUtils utils = new CiteUtils()
            f.text = utils.ttlOlympicSynchronisms()

        } catch (Exception e) {
            throw e
        }
    }
}
