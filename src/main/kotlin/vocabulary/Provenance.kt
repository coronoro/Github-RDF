package vocabulary

import ghproperty.GH
import ghproperty.Namespaces
import org.apache.jena.ontology.OntModel
import org.apache.jena.rdf.model.ModelFactory

object Provenance : Vocabulary(){

    val m = ModelFactory.createOntologyModel()

     val ACTIVITY = m.createClass(Namespaces.PROVO.uri+"Activity")
     val AGENT = m.createClass(Namespaces.PROVO.uri+"Agent")
     val ENTITY = m.createClass(Namespaces.PROVO.uri+"Entity")

    val STARTED_AT_TIME = m.createDatatypeProperty(Namespaces.PROVO.uri+"startedAtTime")
    val GENERATED_AT_TIME = m.createDatatypeProperty(Namespaces.PROVO.uri+"generatedAtTime")
    val WAS_ASSOCIATED_WITH = m.createDatatypeProperty(Namespaces.PROVO.uri+"wasAssociatedWith")


}