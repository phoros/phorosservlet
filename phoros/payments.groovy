import edu.holycross.shot.phoros.QueryGenerator


import groovyx.net.http.*
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

String sparql = "@sparqls@"
QueryGenerator qg = new QueryGenerator()

/** Class modelling GeoJSON's geographic data model.
 */
class Site {
  def properties
  def geometry
  def type


  String toString() {
    return """${properties.siteName} with coords ${geometry.coordinates}"""
  }
}


String getSparqlReply(String acceptType, String query) {
  String replyString
  def encodedQuery = URLEncoder.encode(query)
  def q = "@sparqls@query?query=${encodedQuery}"
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



response.setContentType("application/json")
response.setCharacterEncoding('UTF-8')
response.setHeader( "Access-Control-Allow-Origin", "*")



def slurper = new groovy.json.JsonSlurper()
String q =  qg.phorosQuery()
def siteReply = slurper.parseText(getSparqlReply("application/json", q))


def featureList = []


def payList = [:]
def siteName = ""
String urn = ""
def coords =  []

siteReply.results.bindings.each { b ->
  if (urn != b.urn.value) {
    if ((urn != "") && (coords.size() > 1)) {
      Site s = new Site(geometry: [:], properties: [:], type: 'Feature')
      s.properties = ['siteName': siteName, 'urn': urn]
      payList.keySet().each { k ->
	s.properties[k] = payList[k]
      }
      s.geometry = ['coordinates': [coords[0], coords[1]], 'type': 'Point']
      featureList.add(s)
      payList.clear()
    }


    if (b.lat?.value) {
      siteName = b.lab.value
      coords.clear()
      coords = [b.lon?.value.toBigDecimal(), b.lat?.value.toBigDecimal() ]
    } 
  }
  payList["year_${b.yr?.value}"] = b.obs?.value
  urn = b.urn.value
}

// don't forget last one!
Site s = new Site(geometry: [:], properties: [:], type: 'Feature')
s.properties = ['siteName': siteName, 'urn': urn, 'payments': payList]
payList.keySet().each { k ->
  s.properties[k] = payList[k]
}
s.geometry = ['coordinates': [coords[0], coords[1]], 'type': 'Point']
featureList.add(s)



def jsonBldr = new groovy.json.JsonBuilder()

jsonBldr(type: 'FeatureCollection', features: featureList)
println jsonBldr.toPrettyString()



