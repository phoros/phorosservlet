package edu.holycross.shot.phoros


class QueryGenerator {

  QueryGenerator() {}



  String siteQuery(String urnStr) {
    return """SELECT ?lab ?lon ?lat ?payrec ?yr ?obs  ?txt WHERE {
?s <http://www.homermultitext.org/hmt/citedata/places_Lon> ?lon .
?s <http://www.homermultitext.org/hmt/citedata/places_Lat> ?lat .
?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?lab .
?s <http://shot.holycross.edu/phoros/rdf/paid>  ?payrec .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Year> ?yr .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Obols>  ?obs .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_TextPassage> ?txt .

FILTER (str(?s) = "${urnStr}") 
}
ORDER BY ?yr 
"""
  }

}