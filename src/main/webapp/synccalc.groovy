import edu.holycross.shot.chron.*
import groovy.xml.MarkupBuilder
import edu.harvard.chs.cite.CiteUrn




String contentType = "text/html"
response.setContentType(contentType)
response.setHeader( "Access-Control-Allow-Origin", "*")


String texts = "@texts@"

String serverUrl = "@tripleserver@"
JGraph jg = new JGraph(serverUrl)


// do some error checking on this ...
//String model = params.model
//StringBuffer csv = new StringBuffer("source,target,value,url\n");


String f = params.f
def confDir = context.getRealPath("/")
File syncFile = new File(confDir + "/" + f)

StringWriter writer = new StringWriter()
MarkupBuilder html = new MarkupBuilder(writer)


def synchronisms = []
def count = 0
syncFile.eachLine {  l ->
    
    if (count > 0) {
        def cols = l.split(/,/)
        synchronisms.add(cols)
    }
    count++
}

//String testCsv = "source,target,value,url\na,b,1,url\na,c,1,url2\nb,c,1,url3,a,d,1,url4\n"

html.html {
    head {
	link(type : 'text/css',  rel : 'stylesheet',  href : 'css/chron.css', title : 'CSS stylesheet')
        mkp.yield "\nINSERTSCRIPT"
        title("Analyze chronology")
    }
    
    body {
        mkp.yield "\nBODYSCRIPT\n"
    	header {
            h1 {
                mkp.yield "Analyze chronology"
            }
            nav (role: 'navigation') {
                mkp.yield "Chronometer: "
                a(href: "home",  "home")
            }

        }
    	
        article {
            div {
                p("Data from file ${f}")
            }
            div (style : "float:right;width:40%;") {

                ul {
                    synchronisms.each { syn ->
                        li {
                            String evt1 = jg.getLabel(syn[1])
                            String evt2 = jg.getLabel(syn[4])
                            
                            if (syn.size() > 5) {
                                mkp.yield "${evt1} ${syn[2]} ${evt2} by ${syn[5]}: "
                            } else {
                                mkp.yield "${evt1} ${syn[2]} ${evt2}: "
                            }
                            a (href : "${texts}?request=GetPassagePlus&urn=${syn[0]}", "source")

                        }
                    }
                    
                }
            }
        }
    }
}


String base = writer.toString()
base = base.replaceAll('INSERTSCRIPT','<script src="http://d3js.org/d3.v3.js"></script>')


String bodyScript = """

<script>

// get the data
d3.csv("labelchroncsv.groovy?f=${f}", function(error, links) {

var nodes = {};

// Compute the distinct nodes from the links.
links.forEach(function(link) {
    link.source = nodes[link.source] || 
        (nodes[link.source] = {name: link.source, url: link.url});
    link.target = nodes[link.target] || 
        (nodes[link.target] = {name: link.target, url : link.url});
    //link.units = +link.units;
link.units = 1;
console.log(link);
});
//console.log(nodes);
//console.log("d3.values.nodes")
//console.log(d3.values(nodes));
var width = 500,
    height = 300;

var force = d3.layout.force()
    .nodes(d3.values(nodes))
    .links(links)
    .size([width, height])
    .linkDistance(60)
    .charge(-300)
    .on("tick", tick)
    .start();

// Set the range
var  v = d3.scale.linear().range([0, 100]);

// Scale the range of the data
v.domain([0, d3.max(links, function(d) { return d.units; })]);


var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);

// build the arrow.
svg.append("svg:defs").selectAll("marker")
    .data(["end"])      // Different link/path types can be defined here
  .enter().append("svg:marker")    // This section adds in the arrows
    .attr("id", String)
    .attr("viewBox", "0 -5 10 10")
    .attr("refX", 15)
    .attr("refY", -1.5)
    .attr("markerWidth", 6)
    .attr("markerHeight", 6)
    .attr("orient", "auto")
  .append("svg:path")
    .attr("d", "M0,-5L10,0L0,5");

// add the links and the arrows
var path = svg.append("svg:g").selectAll("path")
    .data(force.links())
  .enter().append("svg:path")
    .attr("class", function(d) { return "link " + d.type; })
    .attr("marker-end", "url(#end)");

// define the nodes
var node = svg.selectAll(".node")
    .data(force.nodes())
  .enter().append("svg:a") // Append legend elements
          .attr("xlink:href",function(d) {return d.url} )
    .append("g")
    .attr("class", "node")
    .on("click", click)
    .on("dblclick", dblclick)
    .call(force.drag);

// add the nodes
node.append("circle")
    .attr("r", 5);

// add the text 
node.append("text")
    .attr("x", 12)
    .attr("dy", ".35em")
    .text(function(d) { return d.name; });

// add the curvy lines
function tick() {
    path.attr("d", function(d) {
        var dx = d.target.x - d.source.x,
            dy = d.target.y - d.source.y,
            dr = Math.sqrt(dx * dx + dy * dy);
        return "M" + 
            d.source.x + "," + 
            d.source.y + "A" + 
            dr + "," + dr + " 0 0,1 " + 
            d.target.x + "," + 
            d.target.y;
    });

    node
        .attr("transform", function(d) { 
		    return "translate(" + d.x + "," + d.y + ")"; });
}

// action to take on mouse click
function click() {
    d3.select(this).select("text").transition()
        .duration(750)
        .attr("x", 22)
        .style("fill", "steelblue")
        .style("stroke", "lightsteelblue")
        .style("stroke-width", ".5px")
        .style("font", "20px sans-serif");
    d3.select(this).select("circle").transition()
        .duration(750)
        .attr("r", 16)
        .style("fill", "lightsteelblue");
}

// action to take on mouse double click
function dblclick() {
    d3.select(this).select("circle").transition()
        .duration(750)
        .attr("r", 6)
        .style("fill", "#ccc");
    d3.select(this).select("text").transition()
        .duration(750)
        .attr("x", 12)
        .style("stroke", "none")
        .style("fill", "black")
        .style("stroke", "none")
        .style("font", "10px sans-serif");
}

});

</script>

"""



base = base.replaceAll('INSERTSCRIPT','<script src="http://d3js.org/d3.v3.js"></script>')
base = base.replaceAll('BODYSCRIPT', bodyScript)

println base.replaceAll('INSERTFOOTER',"""@htmlfooter@""")


