package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn


class OQueryGenerator {

    OQueryGenerator () {
    }


    /** Composes query string to retrieve a full Observation record
    * including equivalent in EgyptianDate. 
    * @param urnStr Urn value of the observation to retrieve.
    * @returns SPARQL query string.
    */
    String getFullObservationQuery(String urnStr) {
        return """

SELECT  ?urn ?label ?day ?month ?yr ?ruler ?place ?adbcyr ?gregdate WHERE {

?urn  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>     ?label .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Day>       ?day .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Month>  ?month .
?urn  <http://www.homermultitext.org/hmt/citedata/pedersen_Year> ?yr .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Ruler>  ?ruler .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Place>   ?place .

?urn <http://www.homermultitext.org/hmt/citedata/pedersen_ModernYear> ?adbcyr .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Date>  ?gregdate .

FILTER (str(?urn) = "${urnStr}" )  .
}
"""
    }



    /** Composes a query string to retrieve all full Observation records
    * including equivalent in EgyptianDate. 
    */
    String getAllFullObservationsQuery() {
        return """

SELECT ?urn ?label ?day ?month ?yr ?ruler ?place ?adbcyr ?gregdate WHERE {

?urn <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>     ?label .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Day>       ?day .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Month>  ?month .
?urn  <http://www.homermultitext.org/hmt/citedata/pedersen_Year> ?yr .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Ruler>  ?ruler .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Place>   ?place .

?urn <http://www.homermultitext.org/hmt/citedata/pedersen_ModernYear> ?adbcyr .
?urn <http://www.homermultitext.org/hmt/citedata/pedersen_Date>  ?gregdate .
}
"""
}


    /** Composes a query string to return URN and labels for 
    * all objects in the collection.
    */
    String getAllObservationsQuery() {
        return """
SELECT ?urn ?label ?yr WHERE {

 ?urn <http://www.homermultitext.org/hmt/citedata/pedersen_ModernYear> ?yr .
 ?urn <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?label .
 FILTER (regex(str(?urn), "urn:cite:chron:pedersen" ) ) .
}
"""
    }



}
