import data.LoadProjects
import ghproperty.GH
import org.apache.jena.arq.querybuilder.SelectBuilder
import org.apache.jena.atlas.logging.LogCtl
import org.apache.jena.fuseki.embedded.FusekiServer
import org.apache.jena.query.DatasetFactory
import org.apache.jena.sparql.core.DatasetGraph
import org.apache.jena.sparql.core.DatasetGraphFactory
import writer.RDFModelWriter
import java.io.File
import java.io.FileOutputStream
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.query.QuerySolution
import org.apache.jena.enhanced.BuiltinPersonalities.model
import org.apache.jena.fuseki.FusekiLogging
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryFactory
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.system.Txn







val outPath = "C:\\Users\\Coronoro\\Desktop\\gh.ttl"
//val outPath = "C:\\Users\\Tim Streicher\\gh.ttl"

fun main(args : Array<String>) {
    val file = File(outPath)
    file.createNewFile()
    val stream = FileOutputStream(file)

    val writer = RDFModelWriter(stream)

    LoadProjects.loadProjects(writer, 1)

    /*

    var sparql =    "PREFIX gh:<https://github.com/rdf/#> " +
                    "SELECT ?Entity " +
                    "WHERE {" +
                    "?Entity  gh:NAME \"grit\""+
                    "} ";

    */

    var sparql =    "PREFIX gh:<https://github.com/rdf/#> " +
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
                    "SELECT ?Entity ?l" +
                    "WHERE { " +
                    "?Entity  rdfs:member ?l"+
                    //"FILTER(?bag )"+
                    "} ";

    val qry = QueryFactory.create(sparql);
    val qe = QueryExecutionFactory.create(qry, writer.model);
    val rs = qe.execSelect();

    while(rs.hasNext()) {
        val sol = rs.nextSolution();
        sol.varNames().forEach {
            val get = sol.get(it)
            print(get)
        }
    }

    qe.close();


    /*
    QueryExecutionFactory.create(q, writer.model).use({ qexec ->
        val results = qexec.execSelect()
        while (results.hasNext()) {
            val soln = results.nextSolution()
            val x = soln.get("varName")       // Get a result variable by name.
            val r = soln.getResource("VarR") // Get a result variable - must be a resource
            val l = soln.getLiteral("VarL")   // Get a result variable - must be a literal
        }
    })
*/
/*
    val ds = DatasetFactory.createTxnMem()
    ds.addNamedModel("gh", writer.model)
    val server = FusekiServer.create()
            .add("/ds", ds)
            .
            .build()
    server.start()

*/
}


