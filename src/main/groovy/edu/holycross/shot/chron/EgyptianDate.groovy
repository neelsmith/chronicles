package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

/** A class a date within a year in the
* Egyptian 365-day calendar.
*/
class EgyptianDate {

    Integer debug = 0

    /** Month of year */
    EgyptianMonth month
    
    /** Day of month, must be between 1-30 inclusive. */
    Integer day


    LinkedHashMap monthNames = ["thoth" : EgyptianMonth.THOTH ]

    //PHAOPHI, ATHYR, CHOIAK, TYBI, MECHIR, PHAMENOTH, PARMOUTHI, PACHON, PAYNI, EPIPHI, MESORE]


    EgyptianDate(EgyptianMonth m, Integer d) 
    throws Exception {
        if ((d < 1) || (d > 30)) {
            throw new Exception ("EgyptianDate exception:  day number must be between 1 and 30 inclusive.")
        }
        this.month = m
        this.day = d
    }


EgyptianDate(String monthName, Integer d) 
    throws Exception {
        if ((d < 1) || (d > 30)) {
            throw new Exception ("EgyptianDate exception:  day number must be between 1 and 30 inclusive.")
        }
    this.day = d

    String lower = monthName.toLowerCase()
    if (! monthNames.containsKey(lower)) {
        throw new Exception ("EgyptianDate exception: ${monthName} not a valid month name.")
    }
    this.month = monthNames[lower]
    }


}
