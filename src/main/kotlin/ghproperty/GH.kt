package ghproperty

import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property

object GH{

    val uri = "https://github.com/rdf/#"
    private val m = ModelFactory.createDefaultModel()



    public val LANGUAGES = m.createProperty(uri , "LANGUAGES")
    public val COMMITS = m.createProperty(uri , "COMMITS")
    public val CREATED = m.createProperty(uri , "CREATED")
    public val COMMIT_DATE = m.createProperty(uri , "COMMITDATE")
    public val AUTHOR = m.createProperty(uri , "AUTHOR")
    public val NAME = m.createProperty(uri , "NAME")



}