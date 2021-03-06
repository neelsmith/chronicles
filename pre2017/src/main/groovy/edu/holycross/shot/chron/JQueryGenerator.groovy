package edu.holycross.shot.chron

import edu.harvard.chs.cite.CiteUrn

/** A class for working with the RDF graph representation of
* Jerome's c
*/
class JQueryGenerator {

    JQueryGenerator () {
    }


    String getRdfLabelQuery(String urnStr) {
        return """
SELECT ?label WHERE {
?urn  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?label .
FILTER (str(?urn) = "${urnStr}")
}
"""
    }


String getObservationsQuery() {
return """
SELECT ?urn ?label ?yr WHERE {

 ?urn <http://www.homermultitext.org/hmt/citedata/pedersen_ModernYear> ?yr .
 ?urn <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?label .
 FILTER (regex(str(?urn), "urn:cite:chron:pedersen" ) ) .
}
"""
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
    
    String getRulersForFilumQuery(String filumUrn) {
    return """
SELECT ?ruler ?rulerlabel  {

?filum  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://shot.holycross.edu/chron/rdf/Filum> .
?filum <http://www.homermultitext.org/cite/rdf/possesses>  ?rulerdot .
?ruler <http://www.w3.org/2002/07/owl#sameAs>  ?rulerdot .

?rulerdot  <http://purl.org/ontology/olo/core#item> ?seq .

?ruler <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?rulerlabel .

FILTER(str(?filum) = "${filumUrn}")
}

ORDER BY ?seq
"""
}
/*
return """
SELECT ?rulerdot ?ruler ?rulerlabel ?colllabel  {

?filum  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://shot.holycross.edu/chron/rdf/Filum> .
?filum <http://www.homermultitext.org/cite/rdf/possesses>  ?rulerdot .
?ruler <http://www.w3.org/2002/07/owl#sameAs>  ?rulerdot .

?rulerdot  <http://purl.org/ontology/olo/core#item> ?seq .


?ruler <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?rulerlabel .
?rulercollection <http://www.homermultitext.org/cite/rdf/possesses>  ?ruler .


?rulercollection <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?colllabel .

?filum <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?filumlabel .
FILTER(str(?filum) = "${rulerUrn}")
}
ORDER BY ?seq
"""
*/
/*
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
*/


    String getYearsForRulerQuery(String rulerUrn) {
return """
SELECT ?yr  ?label  WHERE {


?yr <http://www.homermultitext.org/cite/rdf/memberOf> ?ruler .
?yr <http://purl.org/ontology/olo/core#item>  ?seq .
?yr <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?label .
FILTER(str(?ruler) = "${rulerUrn}") 

}
ORDER BY ?seq
"""
    }

String labelQuery(String urnStr) {
return """
SELECT ?label WHERE {
?u <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label .
FILTER (str(?u)  = "${urnStr}")
}
"""
}

String getPrevQuery(String urnStr) {
return """
SELECT ?prev  WHERE {
?u  <http://purl.org/ontology/olo/core#previous>  ?prev .
FILTER (str(?u)  = "${urnStr}")
}
"""
}

String getNextQuery(String urnStr) {
return """
SELECT ?nxt  WHERE {
?u  <http://purl.org/ontology/olo/core#next>  ?nxt .
FILTER (str(?u)  = "${urnStr}")
}
"""
}

    String getSyncsForSequence(String urn) {
return """
SELECT ?urn ?label ?chronseq ?seqlabel  ?nxt ?prev  ?sync ?synclabel WHERE {


?urn <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?label .

?urn <http://www.homermultitext.org/cite/rdf/belongsTo>  ?chronseq .
?chronseq <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?seqlabel .

OPTIONAL {

?urn <http://www.w3.org/2002/07/owl#sameAs> ?sync .
?sync <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?synclabel .
}

FILTER(str(?urn) = "${urn}")
}
"""
}
// ?urn  <http://shot.holycross.edu/chron/rdf/synchronizedWith> ?sync .
    String getSyncsForOlympiadQuery(String olympiadYear) {
return """
SELECT ?label ?yr ?label2  ?yr2 ?ruler WHERE {

?yr  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label .
?yr  <http://www.homermultitext.org/cite/rdf/memberOf> ?ruler  .
?yr  <http://shot.holycross.edu/chron/rdf/synchronizedWith>  ?yr2 .
?yr2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label2 .


FILTER(str(?yr) = "${olympiadYear}") 

}
"""
}




    String getSyncsForYearQuery(String rulerYearUrn) {
return """
SELECT ?label ?yr ?label2  ?yr2 ?ruler WHERE {

?yr  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label .
?yr  <http://www.homermultitext.org/cite/rdf/memberOf> ?ruler  .
?yr  <http://shot.holycross.edu/chron/rdf/synchronizedWith>  ?yr2 .
?yr2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label2 .


?ruler  <http://www.w3.org/2002/07/owl#sameAs>  ?r .


FILTER(str(?yr) = "${rulerYearUrn}") 

}
"""
}

    String getSyncsForRulerQuery(String rulerUrn) {
return """
SELECT ?label ?ruler ?seq ?label2  ?yr2 WHERE {

?yr  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label .
?yr  <http://www.homermultitext.org/cite/rdf/memberOf> ?ruler  .
?yr  <http://shot.holycross.edu/chron/rdf/synchronizedWith>  ?yr2 .
?yr2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?label2 .
?yr  <http://purl.org/ontology/olo/core#item>  ?seq .

?ruler  <http://www.w3.org/2002/07/owl#sameAs>  ?r .


FILTER(str(?ruler) = "${rulerUrn}") 

}
ORDER BY ?seq
"""
    }

}
