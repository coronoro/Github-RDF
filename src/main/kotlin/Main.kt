import data.LoadProjects
import org.apache.jena.fuseki.embedded.FusekiServer
import org.apache.jena.query.DatasetFactory
import writer.RDFModelWriter
import java.io.File
import java.io.FileOutputStream
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory


val outPath = "C:\\Users\\Coronoro\\Desktop\\gh2.ttl"
val loadPath = "C:\\Users\\Coronoro\\Desktop\\"
//val outPath = "C:\\Users\\Tim Streicher\\gh2.ttl"



fun main(args : Array<String>) {
    val model = load()
    //val model = loadFile()

    var sparql =    "PREFIX ghrdf:<https://github.com/rdf#> " +
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
                    "SELECT ?Entity " +
                    "WHERE {" +
                    "?Entity  rdfs:label \"grit\""+
                    "} ";


    val sparql1 = "PREFIX provo: <https://www.w3.org/ns/prov-o#>" +
            "PREFIX ghrdf: <https://github.com/rdf#> " +
            "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
            "SELECT ?repo " +
            "WHERE {" +
            "?repo  ghrdf:usesLanguage \"HTML\"" +
            "} "


    val sparql2 = "PREFIX provo: <https://www.w3.org/ns/prov-o#>\n" +
            "PREFIX ghrdf: <https://github.com/rdf#> \n" +
            "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" +
            "\tSELECT ?user \n" +
            "\t\tWHERE {\n" +
            "\t\t\t?user a ghrdf:User\n" +
            "\t\t} \t"

    var sparql3 = "PREFIX provo: <https://www.w3.org/ns/prov-o#>\n" +
            "PREFIX ghrdf: <https://github.com/rdf#> \n" +
            "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
            "\tSELECT ?commit\n" +
            "\t\tWHERE {\n" +
            "\t\t\t?commit provo:wasAssociatedWith ?user .\n" +
            "\t\t\t?user foaf:name \"Dorian\" . \n" +
            "\t\t} "

    var sparql4 = "PREFIX provo: <https://www.w3.org/ns/prov-o#>\n" +
            "PREFIX ghrdf: <https://github.com/rdf#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
            "\tSELECT ?c\n" +
            "\t\tWHERE {\n" +
            "\t\t\t?repo rdfs:label \"grit\" .\n" +
            "\t\t\t?repo ghrdf:commits ?commits .\n" +
            "\t\t\t?commits rdfs:member ?c . \n" +
            "\t\t} "

    var sparql5 = "PREFIX provo: <https://www.w3.org/ns/prov-o#>\n" +
            "PREFIX ghrdf: <https://github.com/rdf#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
            "\tSELECT ?repo (COUNT(?c) AS ?cc)  \n" +
            "\t\tWHERE {\n" +
            "\t\t\t?repo ghrdf:commits ?commits .\n" +
            "\t\t\t?commits rdfs:member ?c . \n" +
            "\t\t\t?c provo:endedAtTime ?date FILTER ( ?date < \"2008-08-01T16:32:34Z\"^^xsd:dateTime ) . \n" +
            "\t\t} GROUP BY ?repo\t"

    var sparql6 = "PREFIX provo: <https://www.w3.org/ns/prov-o#>\n" +
            "PREFIX ghrdf: <https://github.com/rdf#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
            "SELECT ?repo (COUNT(?c) AS ?cc)\n" +
            "WHERE {\n" +
            "\t?repo ghrdf:commits ?commits ;\n" +
            "\t\tghrdf:usesLanguage \"Java\" .\n" +
            "\t?commits rdfs:member ?c . \n" +
            "\t?c provo:endedAtTime ?date FILTER ( ?date < \"2008-08-01T16:32:34Z\"^^xsd:dateTime ) . \t\t\n" +
            "}GROUP BY ?repo HAVING (?cc > 5)"

    query(model, sparql6)
}


fun query(model: Model, query:String){

    val qry = QueryFactory.create(query);
    print(qry)
    val qe = QueryExecutionFactory.create(qry, model);

    val rs = qe.execSelect();

    var head = true
    while(rs.hasNext()) {
        val sol = rs.nextSolution();

        if (head){
            sol.varNames().forEach {
                print(String.format("%-60s",it))
                print("\t")
            }
            head = false
            println()
        }
        sol.varNames().forEach {
            val get = sol.get(it)
            print(String.format("%-60s",get))
            print("\t")

        }
        println()
    }

    qe.close();

}

fun startServer(model: Model){
    val ds = DatasetFactory.createTxnMem()
    ds.addNamedModel("gh", model)
    val server = FusekiServer.create()
            .add("/ds", ds)

            .build()
    server.start()
}

fun loadFile():Model{
    val model = ModelFactory.createDefaultModel()
    return model.read(outPath)
}

fun load():Model{
    val file = File(outPath)
    file.createNewFile()
    val stream = FileOutputStream(file)

    val writer = RDFModelWriter(stream,-1)
    LoadProjects.limit()
    LoadProjects.loadProjects(writer, 100)
    return writer.model
}


