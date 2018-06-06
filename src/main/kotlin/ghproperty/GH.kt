package ghproperty

import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property

object GH{

    private val m = ModelFactory.createOntologyModel()



    public val LANGUAGES = m.createProperty(Namespaces.GHRDF.uri , "LANGUAGES")
    public val COMMITS = m.createProperty(Namespaces.GHRDF.uri , "COMMITS")
    public val CREATED = m.createProperty(Namespaces.GHRDF.uri , "CREATED")
    public val COMMIT_DATE = m.createProperty(Namespaces.GHRDF.uri , "COMMITDATE")
    public val AUTHOR = m.createProperty(Namespaces.GHRDF.uri , "AUTHOR")
    public val NAME = m.createProperty(Namespaces.GHRDF.uri , "NAME")




}