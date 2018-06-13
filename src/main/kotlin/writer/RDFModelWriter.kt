package writer

import vocabulary.GH
import ghproperty.Namespaces
import ghproperty.OntologyClasses
import org.apache.jena.datatypes.xsd.XSDDatatype
import org.apache.jena.datatypes.xsd.XSDDateTime
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.apache.jena.sparql.vocabulary.FOAF
import org.apache.jena.vocabulary.RDFS
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import vocabulary.Provenance
import java.io.OutputStream
import java.util.*
import org.apache.jena.sparql.function.library.date
import java.util.GregorianCalendar
import java.util.Calendar




class RDFModelWriter(val stream:OutputStream, val commitLimit: Int = -1){

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
            clazz.setSuperClass(it.superClass)
        }
    }

    private fun createOntoPredicates(){
        enumValues<OntologyClasses>().forEach {
            val clazz = model.createClass(it.uri)

        }
    }


    fun add(repo: GHRepository){
        val resource = model.createIndividual(repo.htmlUrl.toString(), model.getOntClass(OntologyClasses.REPO.uri))
        resource.addProperty(RDFS.label, repo.name)
        try {
            var loc = 0.0
            for (lang in repo.listLanguages()){
                loc += lang.value
                resource.addProperty(GH.USES_LANGUAGE, lang.key)
            }

            resource.addLiteral(GH.LOC, loc.toInt())

            if (repo.createdAt != null){
                val typedLiteral = model.createTypedLiteral(repo.createdAt, XSDDatatype.XSDdateTime)
                resource.addProperty(Provenance.GENERATED_AT_TIME, typedLiteral)
            }

            var created:Date? = null

            val commitBag = model.createBag()
            resource.addProperty(GH.COMMITS,commitBag)
            var counter = 0
            try {
                for (commit in repo.listCommits()){
                    if (commitLimit > 0 && counter > commitLimit){
                        break
                    }
                    //fallback if there is no creationdate for the project
                    if (created == null && commit.parentSHA1s.isEmpty()){
                        created = commit.authoredDate
                    }
                    commitBag.add(this.add(commit,repo))
                    counter++
                }
            }catch (e: Exception){

            }


            if (created != null){
                resource.addLiteral(Provenance.STARTED_AT_TIME, created)
            }
        }catch (e: Exception){
            println(e.printStackTrace())

        }

    }

    fun addLanguage(language:String):Resource{
        //TODO
        //val userURI = Namespaces.GH .htmlUrl.toString()
        val userURI = Namespaces.GH.uri
        var resource = model.getIndividual(userURI) ?: model.createIndividual(userURI,model.getOntClass(OntologyClasses.USER.uri))
        return resource
    }


    fun add(commit:GHCommit, repo:GHRepository): Resource {
        val baseURI = repo.htmlUrl.toString()  + "/commit/"
        val commitURI = baseURI + commit.shA1
        var resource = model.getResource(commitURI)
        //if Resource already exists then there's noo need to create it again
        if (resource == null){
            resource =  model.createResource(commitURI, model.getOntClass(OntologyClasses.COMMIT.uri))

            resource.addLiteral(GH.LINES_ADDED,commit.linesAdded)
            resource.addLiteral(GH.LINES_DELETED,commit.linesDeleted)


            for (parent in commit.parents) {
                //val res = add(parent, repo)
                val res = baseURI + parent.shA1
                resource.addProperty(GH.PREVIOUS_COMMIT, res)
            }

            //TODO message
            //resource.addProperty(GH.COMMIT_MESSAGE, commit.)
            val typedAuthoredDate = model.createTypedLiteral(commit.authoredDate, XSDDatatype.XSDdateTime)
            resource.addLiteral(Provenance.STARTED_AT_TIME , typedAuthoredDate)

            val myCal = GregorianCalendar()
            myCal.time = commit.commitDate

            //val typedCommitDate = model.createTypedLiteral(commit.commitDate, XSDDatatype.XSDdateTime)
            resource.addLiteral(Provenance.ENDED_AT_TIME , XSDDateTime(myCal))

            if (commit.author != null){
                val author = this.add(commit.author)
                resource.addProperty(Provenance.WAS_ASSOCIATED_WITH, author)
            }
        }

        return resource
    }


    fun add(user:GHUser):Resource{
        val userURI = user.htmlUrl.toString()
        if (user.name == null){

        }
        var resource = model.getIndividual(userURI) ?: model.createIndividual(userURI,model.getOntClass(OntologyClasses.USER.uri))
        var name = user.name
        if (name == null){
            name = userURI.substring(userURI.lastIndexOf("/") .. userURI.length)
        }
        resource.addProperty(FOAF.name, name)
        if (user.email != null){
            resource.addProperty(FOAF.mbox, user.email)
        }

        return resource
    }


    fun write(){
        model.write(stream ,Lang.TTL.name)
        //model.write(stream ,Lang.RDFXML.name)
        //model.write(stream , Lang.N3.name)
        //model.write(stream , Lang.NTRIPLES.name)
    }


}