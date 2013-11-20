package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

/** A class for working with the RDF graph representation of
* Jerome's c
*/
class JQueryGenerator {

    JQueryGenerator () {
    }

    String getFilaQuery() {
        return """
        SELECT ?f  ?label WHERE {
?f <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://shot.holycross.edu/chron/rdf/Filum> . 
?f  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?label .
}
ORDER BY ?label
"""
    }
    
    String getRulersForFilumQuery(String rulerUrn) {
        return """
SELECT ?filum ?filumlabel ?ruler  ?rulerlabel  {
?filum  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://shot.holycross.edu/chron/rdf/Filum> .
?filum <http://www.homermultitext.org/cite/rdf/possesses>  ?rulerdot .
?ruler <http://www.w3.org/2002/07/owl#sameAs>  ?rulerdot .

?rulerdot  <http://purl.org/ontology/olo/core#item> ?seq .


?rulerdot <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?rulerlabel .
?filum <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?filumlabel .
FILTER(str(?filum) = "${rulerUrn}")
}
ORDER BY ?seq
"""
    }


    String getYearsForRulerQuery(String rulerUrn) {
return """
SELECT ?label ?ruler ?seq ?label2  ?yr2 WHERE {

?yr  <http://www.w3.org/1999/02/22-rdf-syntax-ns#Label>  ?label .
?yr  <http://www.homermultitext.org/cite/rdf/memberOf> ?ruler  .
?yr  <http://shot.holycross.edu/chron/rdf/synchronizedWith>  ?yr2 .
?yr2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#Label>  ?label2 .
?yr  <http://purl.org/ontology/olo/core#item>  ?seq .

?ruler  <http://www.w3.org/2002/07/owl#sameAs>  ?r .


FILTER(str(?ruler) = "${rulerUrn}") 

}
ORDER BY ?seq
"""
    }
}
