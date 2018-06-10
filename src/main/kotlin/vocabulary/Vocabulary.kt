package vocabulary

import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory

abstract class Vocabulary{

    val m = ModelFactory.createOntologyModel()
}