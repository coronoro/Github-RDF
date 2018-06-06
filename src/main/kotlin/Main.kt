import data.LoadProjects
import org.apache.jena.arq.querybuilder.SelectBuilder
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
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryFactory





val outPath = "C:\\Users\\Coronoro\\Desktop\\gh.ttl"

fun main(args : Array<String>) {
    val file = File(outPath)
    file.createNewFile()
    val stream = FileOutputStream(file)

    val writer = RDFModelWriter(stream)

    LoadProjects.loadProjects(writer, 10)

    val sb = SelectBuilder()
            .addVar("*")
            .addWhere("?s", "?p", "?o")

    val q = sb.build()
    //writer.model.query(q.)


    QueryExecutionFactory.create(q, writer.model).use({ qexec ->
        val results = qexec.execSelect()
        while (results.hasNext()) {
            val soln = results.nextSolution()
            val x = soln.get("varName")       // Get a result variable by name.
            val r = soln.getResource("VarR") // Get a result variable - must be a resource
            val l = soln.getLiteral("VarL")   // Get a result variable - must be a literal
        }
    })


    val ds = DatasetFactory.createTxnMem()
    ds.addNamedModel("http://github.com/rdf", writer.model )
    val server = FusekiServer.create()
            .add("/ds", ds)
            .build()
    server.start()
}


