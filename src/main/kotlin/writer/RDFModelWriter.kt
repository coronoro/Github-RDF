package writer

import ghproperty.GH
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.VCARD
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import java.io.OutputStream

class RDFModelWriter(val stream:OutputStream, val commitLimit: Int = 10){

    val model = ModelFactory.createDefaultModel()

    fun add(repo: GHRepository){
        val resource = model.createResource(repo.url.toString())
        resource.addProperty(VCARD.NAME, repo.name)
        val languageBag = model.createBag()
        for (lang in repo.listLanguages()){
            languageBag.add(lang)
        }
        resource.addProperty(GH.LANGUAGES, languageBag)
        resource.addProperty(GH.CREATED, repo.createdAt.toString())

        val commitBag = model.createBag()
        resource.addProperty(GH.COMMITS,commitBag)
        var counter = 0
        for (commit in repo.listCommits()){
            if (counter > commitLimit){
                break
            }
            commitBag.add(this.add(commit,repo))
            counter++
        }




    }

    fun add(commit:GHCommit, repo:GHRepository): Resource {
        val resource = model.createResource(repo.url.toString()+"/"+commit.hashCode())

        resource.addProperty(GH.COMMIT_DATE , commit.commitDate.toString())
        if (commit.author != null){
            val author = this.add(commit.author)
            resource.addProperty(GH.AUTHOR, author)
        }
        return resource
    }


    fun add(user:GHUser):Resource{
        val userURI = user.url.toString()
        var resource = model.getResource(userURI) ?: model.createResource()

        resource.addProperty(VCARD.NAME, user.name)
        return resource
    }


    fun write(){
//        model.write(stream ,Lang.TTL.name)
        //model.write(stream ,"RDF/XML-ABBREV")
        model.write(stream , Lang.N3.name)
    }


//    RDFDataMgr.write(System.out, model, Lang.TTL);

}