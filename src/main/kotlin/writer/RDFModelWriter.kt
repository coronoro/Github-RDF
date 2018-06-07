package writer

import ghproperty.GH
import ghproperty.Namespaces
import ghproperty.OntologyClasses
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import vocabulary.Provenance
import java.io.OutputStream


class RDFModelWriter(val stream:OutputStream, val commitLimit: Int = 10){

    val model = ModelFactory.createOntologyModel()

    init{

        model.addLoadedImport("https://www.w3.org/ns/prov-o")
        setPrefixes()

        createOntoClasses()
    }


    private fun setPrefixes(){
        enumValues<Namespaces>().forEach {
            model.setNsPrefix(it.name.toLowerCase(), it.uri)
        }
    }

    private fun createOntoClasses(){
        enumValues<OntologyClasses>().forEach {
            val clazz = model.createClass(it.uri)
        }
    }

    private fun createOntoPredicates(){
        enumValues<OntologyClasses>().forEach {
            val clazz = model.createClass(it.uri)

        }
    }


    fun add(repo: GHRepository){
        val resource = model.createIndividual(repo.htmlUrl.toString(), Provenance.ENTITY)
        resource.addProperty(GH.NAME, repo.name)
        val languageBag = model.createBag()
        for (lang in repo.listLanguages()){
            languageBag.add(lang.key)
        }
        resource.addProperty(GH.LANGUAGES, languageBag)
        if (repo.createdAt != null){
            resource.addProperty(Provenance.GENERATED_AT_TIME, repo.createdAt.toString())
        }

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

        val resource = model.createResource(repo.url.toString()+"/"+commit.shA1, Provenance.ACTIVITY)
        resource.addProperty(GH.COMMIT_PARENT, repo.url.toString()+"/"+commit.parentSHA1s)
        //TODO message
        //resource.addProperty(GH.COMMIT_MESSAGE, commit.)
        resource.addProperty(Provenance.STARTED_AT_TIME , commit.commitDate.toString())
        if (commit.author != null){
            val author = this.add(commit.author)
            resource.addProperty(Provenance.WAS_ASSOCIATED_WITH, author)
        }
        return resource
    }


    fun add(user:GHUser):Resource{
        val userURI = user.url.toString()

        var resource = model.getIndividual(userURI) ?: model.createIndividual(userURI,Provenance.AGENT)
        resource.addProperty(GH.NAME, user.name)
        if (user.email != null){
            resource.addProperty(GH.MAIL, user.email)
        }

        return resource
    }


    fun write(){
        model.write(stream ,Lang.TTL.name)
        //model.write(stream ,"RDF/XML-ABBREV")
        //model.write(stream , Lang.N3.name)
        //model.write(stream , Lang.NTRIPLES.name)
    }


}