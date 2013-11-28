

import edu.holycross.shot.chron.*
import groovy.xml.MarkupBuilder
import edu.harvard.chs.cite.CiteUrn


String contentType = "text/html"
response.setContentType(contentType)
response.setHeader( "Access-Control-Allow-Origin", "*")


File confDir = new File( context.getRealPath("/chronologies"))





StringWriter writer = new StringWriter()
MarkupBuilder html = new MarkupBuilder(writer)






html.html {
    head {
        title("Chronologies")
	link(type : 'text/css',  rel : 'stylesheet',  href : 'css/chron.css', title : 'CSS stylesheet')
    }
    
    body {
    
    	header {
            h1("Chronologies")
            
        }
        nav (role: 'navigation') {
            mkp.yield "Chronometer "
        }

    }
    	
    article {
p("Choose a file with a chronology to analyze:")
def p = ~/.*\.csv/
ul {
    confDir.eachFileMatch(p) { f ->
        li {
            a(href : "synccalc?f=chronologies/${f.name}", "${f.name}")
        }
    }
}

    }
    footer {
        mkp.yield "INSERTFOOTER"
    }
}


String base = writer.toString()
println base.replaceAll('INSERTFOOTER',"""@htmlfooter@""")





    
