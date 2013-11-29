package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

/** A class for working with the RDF graph representation of
* Jerome's c
*/
class OQueryGenerator {

    OQueryGenerator () {
    }


    String getObservationQuery(String urnStr) {
        return """

SELECT ?label ?day ?month ?yr  ?place ?adbcyr ?gregdate WHERE {

?u  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>     ?label .
?u <http://www.homermultitext.org/hmt/citedata/pedersen_Day>       ?day .
?u <http://www.homermultitext.org/hmt/citedata/pedersen_Month>  ?month .
?u  <http://www.homermultitext.org/hmt/citedata/pedersen_Year> ?yr .
?u <http://www.homermultitext.org/hmt/citedata/pedersen_Place>   ?place .

?u <http://www.homermultitext.org/hmt/citedata/pedersen_ModernYear> ?adbcyr .
?u <http://www.homermultitext.org/hmt/citedata/pedersen_Date>  ?gregdate .

        FILTER (str(?u) = "${urnStr}" )  .
}
"""
    }

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
