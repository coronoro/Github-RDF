package vocabulary

import ghproperty.Namespaces
import org.apache.jena.rdf.model.ModelFactory

object Provenance : Vocabulary(){



     val ACTIVITY = m.createClass(Namespaces.PROVO.uri+"Activity")
     val PERSON = m.createClass(Namespaces.PROVO.uri+"Person")
     val ENTITY = m.createClass(Namespaces.PROVO.uri+"Entity")

    val STARTED_AT_TIME = m.createDatatypeProperty(Namespaces.PROVO.uri+"startedAtTime")
    val ENDED_AT_TIME = m.createDatatypeProperty(Namespaces.PROVO.uri+"endedAtTime")
    val GENERATED_AT_TIME = m.createDatatypeProperty(Namespaces.PROVO.uri+"generatedAtTime")
    val WAS_ASSOCIATED_WITH = m.createDatatypeProperty(Namespaces.PROVO.uri+"wasAssociatedWith")


}