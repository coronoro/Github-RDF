import data.LoadProjects
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


    val server = FusekiServer.create()
            .add("/dataset", ds)
            .build()
    server.start()
}


