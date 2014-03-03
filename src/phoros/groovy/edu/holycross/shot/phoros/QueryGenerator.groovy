package edu.holycross.shot.phoros

//import edu.harvard.chs.cite.CtsUrn

class QueryGenerator {

  QueryGenerator() {}



  String allLonLats() {
    return """
SELECT ?site ?sitename ?lat ?lon WHERE {
   ?site <http://www.homermultitext.org/hmt/citedata/places_EnglishForm>   ?sitename .
   ?site <http://www.homermultitext.org/hmt/citedata/places_Lat>  ?lat .
   ?site <http://www.homermultitext.org/hmt/citedata/places_Lon>  ?lon .
}
"""
  }

  /**
   * @param urnStr CTS URN of a *diplomatic* edition.
   */
  String imgsForDoc(String urnStr) {
    String queryStr = urnStr
    /*
    CtsUrn urn

    try {
      urn = new CtsUrn(urnStr)
    } catch (Exception  e) {
      throw e
    }
    String queryStr =  urn.getUrnWithoutPassage()
    if (urn.getVersion() != "hc1") {
      queryStr = "urn:cts:${urn.getCtsNs()}:${urn.getTextGroup()}.${urn.getWork()}.hc1"
    }
    */
    return """
SELECT ?stone ?doc ?defimg ?img WHERE {

?stone <http://www.homermultitext.org/hmt/citedata/documents_CtsUrn>  ?doc .
?stone <http://www.homermultitext.org/hmt/citedata/documents_DefaultImg> ?defimg .
?stone <http://www.homermultitext.org/cite/rdf/illustratedBy>   ?img .
FILTER (str(?doc)= "${queryStr}")
}"""
  }

  String phorosQuery() {
    return """SELECT ?urn ?lab ?lon ?lat ?payrec ?yr ?obs  ?txt WHERE {

?urn <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?lab .
?urn <http://shot.holycross.edu/phoros/rdf/paid>  ?payrec .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Year> ?yr .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Obols>  ?obs .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_TextPassage> ?txt .

OPTIONAL {
?urn <http://www.homermultitext.org/hmt/citedata/places_Lon> ?lon .
?urn <http://www.homermultitext.org/hmt/citedata/places_Lat> ?lat .
}

FILTER (!regex(str(?lab), "^urn:.+\$" ) ) .

}
ORDER BY ?urn 
"""
  }


  String siteQuery(String urnStr) {
    return """SELECT ?lab ?lon ?lat ?payrec ?yr ?obs  ?txt WHERE {

?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?lab .
?s <http://shot.holycross.edu/phoros/rdf/paid>  ?payrec .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Year> ?yr .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Obols>  ?obs .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_TextPassage> ?txt .

OPTIONAL {
?s <http://www.homermultitext.org/hmt/citedata/places_Lon> ?lon .
?s <http://www.homermultitext.org/hmt/citedata/places_Lat> ?lat .
}

FILTER (str(?s) = "${urnStr}") 
}
ORDER BY ?yr 
"""
  }




}