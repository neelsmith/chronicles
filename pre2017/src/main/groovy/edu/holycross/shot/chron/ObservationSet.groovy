package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class ObservationSet {

    String sparqlUrl
        
    OQueryGenerator obsQuery = new OQueryGenerator()

    ArrayList observations = []

    /** */
    ObservationSet(String sparql, boolean collectAll ) 
    throws Exception {
        this.sparqlUrl = sparql
        String obsReply
        if (collectAll) {
            obsReply = getSparqlReply("application/json",obsQuery.getAllObservationsQuery())
        } else {
            obsReply = getSparqlReply("application/json",obsQuery.getAllFullObservationsQuery())
        }
        initSetFromBindings(obsReply)
    }


    ObservationSet(String sparql) {
        this.sparqlUrl = sparql
        String obsReply = getSparqlReply("application/json",obsQuery.getAllFullObservationsQuery())
        initSetFromBindings(obsReply)
    }
    
    void initSetFromBindings(String replyString) {
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(replyString)

        System.err.println "NUM BINDINGS: " + parsedReply.results.bindings.size()

        // Some properties may be null.  Test carefully.
        parsedReply.results.bindings.each { b ->
            Observation obs = new Observation(new CiteUrn(b.urn.value), sparqlUrl)
            observations.add(obs)
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
    String getSparqlReply(String acceptType, String query) {
        String replyString
        def encodedQuery = URLEncoder.encode(query)
        System.err.println "GET REPLY WIH URL " + this.sparqlUrl
        def q = "${this.sparqlUrl}query?query=${encodedQuery}"
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
