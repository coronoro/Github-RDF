package ghproperty

import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property

object GH{

    private val m = ModelFactory.createOntologyModel()



    public val LANGUAGES = m.createProperty(Namespaces.GH.uri , "LANGUAGES")
    public val COMMITS = m.createProperty(Namespaces.GH.uri , "COMMITS")
    public val CREATED = m.createProperty(Namespaces.GH.uri , "CREATED")
    public val COMMIT_DATE = m.createProperty(Namespaces.GH.uri , "COMMITDATE")
    public val COMMIT_PARENT = m.createProperty(Namespaces.GH.uri , "COMMITPARENT")
    public val COMMIT_MESSAGE = m.createProperty(Namespaces.GH.uri , "COMMITMESSAGE")
    public val AUTHOR = m.createProperty(Namespaces.GH.uri , "AUTHOR")
    public val NAME = m.createProperty(Namespaces.GH.uri , "NAME")
    public val MAIL = m.createProperty(Namespaces.GH.uri , "MAIL")




}