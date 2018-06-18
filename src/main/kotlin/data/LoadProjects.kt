package data

import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.RDFDataMgr
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import writer.RDFModelWriter
import java.io.File

object LoadProjects {


    fun loadProjects(writer:RDFModelWriter, limit:Int = 50){
        val github = GitHub.connect()
        var counter = 0
        for (repo in github.listAllPublicRepositories()) {
            if (counter == limit){
                break
            }
            writer.add(repo)
            counter++

        }
        writer.write()
    }

    fun limit(){
        val github = GitHub.connect()
        val limit = github.rateLimit()

        print(limit.remaining)
        print(limit.reset)
    }

}