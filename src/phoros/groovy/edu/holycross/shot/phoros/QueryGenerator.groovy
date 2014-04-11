package edu.holycross.shot.phoros

//import edu.harvard.chs.cite.CtsUrn

class QueryGenerator {

  QueryGenerator() {}

  String phorosWChangeQuery() {
    return phorosWChangeQuery("urn")
  } 

  String phorosWChangeQuery(String sortBy) {
    String sortFields = "?urn"
    if (sortBy == "change") {
      sortFields = "?yr ?seq"
    }  

    return """
SELECT ?urn ?lab ?yr ?obs  ?txturn ?txt ?lon ?lat ?chg ?chgamt ?seq WHERE {

?urn <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?lab .
?urn <http://shot.holycross.edu/phoros/rdf/paid>  ?payrec .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Year> ?yr .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_TextPassage> ?txturn .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_TextContent> ?txt .
?payrec <http://purl.org/ontology/olo/core#item> ?seq .

OPTIONAL {
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_ChangeStatus> ?chg .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_ChangeAmount> ?chgamt .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Obols>  ?obs .
}


OPTIONAL {
?urn  <http://shot.holycross.edu/phoros/rdf/locatedAt>  ?loc .
?loc <http://www.homermultitext.org/hmt/citedata/loc_Lat> ?lat .
?loc <http://www.homermultitext.org/hmt/citedata/loc_Lon> ?lon 
}


}
ORDER BY ${sortFields}

"""
  }

  String phorosSeqQuery() {
    return """SELECT ?site ?lab ?payrec ?yr ?obs  WHERE {

?site <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?lab .
?site <http://shot.holycross.edu/phoros/rdf/paid>  ?payrec .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Year> ?yr .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Obols>  ?obs .
?payrec <http://purl.org/ontology/olo/core#item> ?seq .

FILTER (!regex(str(?lab), "^urn:.+\$" ) ) .

}
ORDER BY ?seq 
"""
  }



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

<${urnStr}> <http://www.w3.org/1999/02/22-rdf-syntax-ns#label>  ?lab .
<${urnStr}> <http://shot.holycross.edu/phoros/rdf/paid>  ?payrec .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Year> ?yr .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_Obols>  ?obs .
?payrec <http://www.homermultitext.org/hmt/citedata/payrec_TextPassage> ?txt .

OPTIONAL {

<${urnStr}>  <http://shot.holycross.edu/phoros/rdf/locatedAt>  ?loc .
?loc <http://www.homermultitext.org/hmt/citedata/loc_Lat> ?lat .
?loc <http://www.homermultitext.org/hmt/citedata/loc_Lon> ?lon 

}

}
ORDER BY ?yr 
"""
  }




}