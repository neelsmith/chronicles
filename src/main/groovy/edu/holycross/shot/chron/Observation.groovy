package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

/** A class modelling astronomical observations in 
* Ptolemy's Almagest
*/
class Observation {

    /** URN for observation. */
    CiteUrn urn

    /** Human-readable label. */
    String label

    /** Date of observation (month and day) as recorded
    * by Ptolemy in the Egyptian calendar. Will be null
    * for obsevations that Ptolemy dates by other means
    * such as Callipan cycle or Athenian archon. */
    EgyptianDate edate

    /** Ruler. Sometimes null. */
    CiteUrn ruler

    /** Year within reign of ruler. Sometimes null.*/
    Integer yrInReign

    /** Place of observation. Sometimes null.*/
    String placeName
    
    /** Date of observation (year, month, day) in the
    * Gregorian system, as computed by Pedersen and Manitius. */
    GregorianCalendar gdate


    /** Utility for accessing enumeration of Calendar 
    * class months by lower case name. */
    LinkedHashMap gMonthNames = [
        "january" : Calendar.JANUARY,
        "february" : Calendar.FEBRUARY,
        "march" : Calendar.MARCH,
        "april" : Calendar.APRIL,
        "may" : Calendar.MAY,
        "june" : Calendar.JUNE,
        "july" : Calendar.JULY,
        "august" : Calendar.AUGUST,
        "september" : Calendar.SEPTEMBER,
        "october" : Calendar.OCTOBER,
        "november" : Calendar.NOVEMBER,
        "december" : Calendar.DECEMBER
    ]

    /** Constructor initializing from a CITE URN
    * and a SPARQL endpoint.
    * @throws Exception if Gregorian date value cannot
    * be instantiated.
    */
    Observation(CiteUrn u, String sparqlUrl) 
    throws Exception {
        this.urn = u

        OQueryGenerator obsQuery = new OQueryGenerator()
        String obsReply = getSparqlReply(sparqlUrl,"application/json",obsQuery.getObservationQuery(u.toString()))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(obsReply)
        
        System.err.println "Query " + obsQuery.getObservationQuery(u.toString())

        // Some properties may be null.  Test carefully.
        parsedReply.results.bindings.each { b ->
            System.err.println "BINDING " + b
            this.label = b.label.value

            if ((b.adbcyr != null) && (b.gregdate != null)) {
                Integer yearNum
                def yrParts = b.adbcyr.value.split(/\s+/)
                def dateParts = b.gregdate.value.split(/\s+/)

                Integer rawYear = yrParts[0] as Integer
                if (yrParts[1] == "BC") {
                    yearNum = (rawYear - 1) * -1
                } else {
                    yearNum = rawYear
                }

                String monthName = dateParts[0].toLowerCase()
                Integer day = dateParts[1] as Integer

                if (! gMonthNames.containsKey(monthName)) {
                    throw new Exception("Observation: unrecognized month name ${dateParts[0]}")
                }
                // check on monthName
                this.gdate = new GregorianCalendar(yearNum, gMonthNames[monthName],day)
            }

            
            if (b.month != null) {
                Number day = b.day.value as BigDecimal
                this.edate = new EgyptianDate(b.month.value, day)
            }

            if (b.place != null) {
                this.placeName =  b.place.value
            }

            if (b.ruler != null) {
                this.ruler =  new CiteUrn(b.ruler.value)
            }

            if (b.yr != null) {
                this.yrInReign = b.yr.value as Integer
            }

        }
    }





    /** Submits a SPARQL query to the configured endpoint
    * and returns the text of the reply.
    * @param acceptType  Value to use for headers.Accept in 
    * http request.  If the value of acceptType is 'applicatoin/json'
    * fuseki's additional 'output' parameter is added to the 
    * http request string so that the string returned for the
    * the request will be in JSON format.  This separates the 
    * concerns of forming SPARQL queries from the decision about
    * how to parse the reply in a given format.
    * @param query Text of SPARQL query to submit.
    * @returns Text content of reply. 
    */
    String getSparqlReply(String tripletServerUrl, String acceptType, String query) {
        String replyString
        def encodedQuery = URLEncoder.encode(query)
        def q = "${tripletServerUrl}query?query=${encodedQuery}"
        if (acceptType == "application/json") {
            q +="&output=json"
        }
        def http = new HTTPBuilder(q)
        
        http.request( Method.GET, ContentType.TEXT ) { req ->
            headers.Accept = acceptType
            response.success = { resp, reader ->
                replyString = reader.text
            }
        }
        return replyString
    }




}
