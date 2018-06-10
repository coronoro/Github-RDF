package ghproperty

import org.apache.jena.ontology.OntClass
import vocabulary.Provenance

enum class OntologyClasses(val uri:String, val superClass:OntClass){

    REPO(Namespaces.GHRDF.uri +"Repo",Provenance.ENTITY),
    COMMIT(Namespaces.GHRDF.uri +"Commit",Provenance.ACTIVITY),
    USER(Namespaces.GHRDF.uri +"User",Provenance.PERSON)

}