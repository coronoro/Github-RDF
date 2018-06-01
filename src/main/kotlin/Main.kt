import data.LoadProjects
import org.apache.jena.fuseki.embedded.FusekiServer
import org.apache.jena.query.DatasetFactory
import org.apache.jena.sparql.core.DatasetGraph
import writer.RDFModelWriter
import java.io.File
import java.io.FileOutputStream



val outPath = "C:\\Users\\Coronoro\\Desktop\\gh.ttl"

fun main(args : Array<String>) {
    val file = File(outPath)
    file.createNewFile()
    val stream = FileOutputStream(file)

    val writer = RDFModelWriter(stream)

    LoadProjects.loadProjects(writer, 10)

    var dsg = DatasetFactory.assemble(writer.model);
    val server = FusekiServer.create()
            .add("/dataset", dsg)
            .build()
    server.start()
}


