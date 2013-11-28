import edu.holycross.shot.chron.*

String contentType = "text/plain"
response.setContentType(contentType)
response.setHeader( "Access-Control-Allow-Origin", "*")


String sparqlUrl = "@tripleserver@"
JGraph jg = new JGraph(sparqlUrl)

String f = params.f
def confDir = context.getRealPath("/")
File syncFile = new File(confDir + "/" + f)

StringBuffer csv = new StringBuffer()

Integer count = 0

//evidence,event1,synchronization,synctype,event2,units
syncFile.eachLine { l->
    if (count == 0) {
        csv.append("${l}\n")
    } else {
        def cols = l.split(/,/)
        csv.append(cols[0] + ",")
        csv.append('"' + jg.getLabel(cols[1]) + '",')
        csv.append(cols[2] + ",")
        csv.append(cols[3] + ",")

        System.err.println "col 2 = #${cols[1]}# converting to ${jg.getLabel(cols[1])}"
        System.err.println "col 5 = #${cols[4]}# converting to ${jg.getLabel(cols[4])}"
        csv.append('"' + jg.getLabel(cols[4]) + '",')
        if (cols.size() > 5) {
            csv.append(cols[5] + "\n")
        } else {
            csv.append("\n")
        }
    }
    count++;
}


println csv.toString()


