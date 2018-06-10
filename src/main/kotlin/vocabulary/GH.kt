package vocabulary

import ghproperty.Namespaces
import org.apache.jena.rdf.model.ModelFactory

object GH : Vocabulary(){




    public val USED_LANGUAGES = m.createProperty(Namespaces.GHRDF.uri , "usedLanguages")
    public val USES_LANGUAGE = m.createProperty(Namespaces.GHRDF.uri , "usesLanguage")
    public val COMMITS = m.createProperty(Namespaces.GHRDF.uri , "commits")

    public val LOC = m.createProperty(Namespaces.GHRDF.uri , "linesOfCode")
    public val LINES_ADDED = m.createProperty(Namespaces.GHRDF.uri , "linesAdded")
    public val LINES_DELETED = m.createProperty(Namespaces.GHRDF.uri , "linesDeleted")


    public val PREVIOUS_COMMIT = m.createProperty(Namespaces.GHRDF.uri , "previousCommit")

    public val MAIL = m.createProperty(Namespaces.GHRDF.uri , "MAIL")




}