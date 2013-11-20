package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

/** A class for working with the RDF graph representation of
* Jerome's c
*/
class JGraph {

    JQueryGenerator jqg


    String tripletServerUrl

    JGraph(String serverUrl) {
        this.tripletServerUrl = serverUrl
        this.jqg = new JQueryGenerator()
    }

    LinkedHashMap getRulersForFilum(CiteUrn urn) {
        return getRulersForFilum(urn.toString())
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

    ArrayList getSynchronismsForRuler(String urnStr) {
        def syncs = []
        String syncReply = getSparqlReply("application/json",jqg.getYearsForRulerQuery(urnStr))
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


    /** Returns an ordered list. */
    ArrayList getRulersForFilum(String urnStr) {
        def filumSequence = []
        String filumReply = getSparqlReply("application/json",jqg.getRulersForFilumQuery(urnStr))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(filumReply)

        parsedReply.results.bindings.each { b ->

            if ((b.ruler) && (b.rulerlabel)) {
                def record  = [b.ruler.value, b.rulerlabel.value]
                filumSequence.add(record)
            }
        }
        return filumSequence
    }



    LinkedHashMap getYearsForRuler() {
        
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
