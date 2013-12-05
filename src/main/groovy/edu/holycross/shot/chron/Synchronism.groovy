package edu.holycross.shot.chron


class Synchronism {


    String evidence
    String src
    String target

    String synchronization
    String synchtype

    String units

    String weight() {
        
        return "PATH FROM ${src} TO ${target}: ${synchronization} by ${units}"
    }


    /** Constructor to initialize a Synchronism
    * from an ordered list of values.
    */
    Synchronism(ArrayList columns) {
        evidence = columns[0]
        src = columns[1]
        synchronization = columns[2]
        synchtype = columns[3]
        target = columns[4]
        units = columns[5]
    }
}
