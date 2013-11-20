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
