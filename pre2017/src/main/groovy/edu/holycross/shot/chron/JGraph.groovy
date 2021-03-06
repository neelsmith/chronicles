package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

/** A class for working with the RDF graph representation of
* Jerome's chronology
*/
class JGraph {

    JQueryGenerator jqg


    String tripletServerUrl

    JGraph(String serverUrl) {
        this.tripletServerUrl = serverUrl
        this.jqg = new JQueryGenerator()
    }

   String getRdfLabel(String urn) {
        String label = ""
        String labelReply = getSparqlReply("application/json",jqg.getRdfLabelQuery(urn))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(labelReply)
        parsedReply.results.bindings.each { b ->
            label = b.label.value
        }
        return label
    }



    ArrayList getSynchronismsForSequence(String urnStr) {
        def syncs = []
        String syncReply = getSparqlReply("application/json",jqg.getSyncsForSequence(urnStr))

        System.err.println "SEQ QUERY : " +  jqg.getSyncsForSequence(urnStr)

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(syncReply)
        parsedReply.results.bindings.each { b ->
            System.err.println "BINDING for " + b.urn.value
            System.err.println b
        }
        return syncs
    }
    


    ArrayList getSynchronismsForRuler(String urnStr) {
        def syncs = []
        String syncReply = getSparqlReply("application/json",jqg.getSyncsForRulerQuery(urnStr))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(syncReply)

        parsedReply.results.bindings.each { b ->
//            if ((b.ruler) && (b.rulerlabel)) {
                def record  = [b.label.value, b.ruler.value, b.label2.value, b.yr2.value]
                syncs.add(record)
//            }
        }
        return syncs
    }



    ArrayList getRulersForFilum(CiteUrn urn) {
        return getRulersForFilum(urn.toString())
    }

    /** Finds rulers for a filum.
    * @param urnStr CiteUrn value identifying filum.
    * @returns An ordered list of urn, label pairs. 
    */
    ArrayList getRulersForFilum(String urnStr) {
        def filumSequence = []
        String filumReply = getSparqlReply("application/json",jqg.getRulersForFilumQuery(urnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(filumReply)


        def currentRuler = ""
        parsedReply.results.bindings.each { b ->
            def ruler = [b.ruler.value, b.rulerlabel.value]
                filumSequence.add(ruler)
            
/*
            if ((b.ruler) && (b.rulerlabel)) {
                def record  = [b.ruler.value, b.rulerlabel.value]
                filumSequence.add(record)
            }
*/
        }
        return filumSequence
    }


    ArrayList getYearsForRuler(CiteUrn urn) {
        return getYearsForRuler(urn.toString())
    }


    /** Finds years for a ruler.
    * @param urnStr CiteUrn value identifying ruler.
    * @returns An ordered list of urn, label pairs. 
    */
    ArrayList getYearsForRuler(String urnStr) {
        def rulerYears = []
        String rulerReply = getSparqlReply("application/json",jqg.getYearsForRulerQuery(urnStr))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(rulerReply)
        parsedReply.results.bindings.each { b ->
            def rulerYear = [b.yr.value, b.label.value]
            rulerYears.add(rulerYear)
        }
        return rulerYears
    }

    String getLabel(CiteUrn urn) {
        return getLabel(urn.toString())
    }
    String getLabel(String urnStr) {
        String label = ""
        String labelReply = getSparqlReply("application/json",jqg.labelQuery(urnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(labelReply)
        parsedReply.results.bindings.each { b ->
            label = b.label.value
        }
        return label
    }


    /** Gets a pair of URN values for previous
    * and next objects in an ordered collection.
    * @param urn Urn of the object to query for.
    * @returns A two-element array containing a pair
    * of URN values as Strings.  The first element is
    * the previous value;  the second element is the
    * next value.  If there is no previous or next element
    * in the collection, ...
    */
    ArrayList getPrevNext(CiteUrn urn) {
        return getPrevNext(urn.toString())
    }

    ArrayList getPrevNext(String urnStr) {
        String prev = ""
        String nxt = ""

        def slurper = new groovy.json.JsonSlurper()

        String prevReply = getSparqlReply("application/json",jqg.getPrevQuery(urnStr))
        def parsedPrevReply = slurper.parseText(prevReply)
        parsedPrevReply.results.bindings.each { b ->
            prev = b.prev.value
        }        

        String nextReply = getSparqlReply("application/json",jqg.getNextQuery(urnStr))
        def parsedNextReply = slurper.parseText(nextReply)
        parsedNextReply.results.bindings.each { b ->
            nxt = b.nxt.value
        }        

        return [prev, nxt]
    }




    ArrayList getSyncsForOlympiadYear(CiteUrn urn) {
        return getSyncsForOlympiadYear(urn.toString())
    }
    ArrayList getSyncsForOlympiadYear(String urnStr) {
        def syncs = []
        String rulerReply = getSparqlReply("application/json",jqg.getSyncsForOlympiadQuery(urnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(rulerReply)
        parsedReply.results.bindings.each { b ->

            def record  = [b.label.value, b.yr.value, b.label2.value, b.yr2.value]
            if (b.yr.value == "") {
                System.err.println "EMPTY YEAR!"
            }
            syncs.add(record)
        }
       return syncs
    }


    ArrayList getSyncsForYear(CiteUrn urn) {
        return getSyncsForYear(urn.toString())
    }

    ArrayList getSyncsForYear(String urnStr) {
        def syncs = []
        String rulerReply = getSparqlReply("application/json",jqg.getSyncsForYearQuery(urnStr))

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(rulerReply)
        parsedReply.results.bindings.each { b ->

            def record  = [b.label.value, b.yr.value, b.label2.value, b.yr2.value]
            if (b.yr.value == "") {
                System.err.println "EMPTY YEAR!"
            }
            syncs.add(record)
        }
       return syncs
    }


    /** Gets list of fila type objects from the graph.
    *  @returns A map of URNs to labels. */
    LinkedHashMap getFila() {
        def fila = [:]
        String filaReply = getSparqlReply("application/json",jqg.getFilaQuery())
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(filaReply)

        parsedReply.results.bindings.each { b ->
            if ((b.f) && (b.label)) {
                fila[b.f.value] = b.label.value
            }
        }
        return fila
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
    String getSparqlReply(String acceptType, String query) {
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
