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

    /** Month of year */
    EgyptianMonth month
    
    /** Day of month, must be between 1-30 inclusive. */
    Number day

    /** Map of lower-case Strings to enumerated values for
    * Egyptian months.
    */
    LinkedHashMap monthNames = [
        "thoth" : EgyptianMonth.THOTH,
        "phaophi": EgyptianMonth.PHAOPHI, 
        "athyr": EgyptianMonth.ATHYR, 
        "choiak": EgyptianMonth.CHOIAK, 
        "tybi": EgyptianMonth.TYBI, 
        "mechir": EgyptianMonth.MECHIR, 
        "phamenoth": EgyptianMonth.PHAMENOTH, 
        "parmouthi": EgyptianMonth.PARMOUTHI, 
        "pachon": EgyptianMonth.PACHON, 
        "payni": EgyptianMonth.PAYNI, 
        "epiphi": EgyptianMonth.EPIPHI, 
        "mesore": EgyptianMonth.MESORE
    ]
   


    /** Constructor taking month object and day number.
    * @param m Enumerated value for month in Egyptian calendar.
    * @param d Day number within month.
    * @throws Exception for invalid values of m or d.
    */
    EgyptianDate(EgyptianMonth m, Integer d) 
    throws Exception {
        if ((d < 1) || (d > 30)) {
            throw new Exception ("EgyptianDate exception:  day number must be between 1 and 30 inclusive.")
        }
        this.month = m
        this.day = d
    }

    /** Constructor taking month object and day number.
    * @param m Enumerated value for month in Egyptian calendar.
    * @param d Day number within month.
    * @throws Exception for invalid values of m or d.
    */
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
