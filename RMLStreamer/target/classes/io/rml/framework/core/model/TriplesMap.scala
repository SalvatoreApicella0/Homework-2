/**
  * MIT License
  *
  * Copyright (C) 2017 - 2020 RDF Mapping Language (RML)
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in
  * all copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  * THE SOFTWARE.
  *
  **/

package io.rml.framework.core.model

import io.rml.framework.core.model.std.{StdStreamTriplesMap, StdTriplesMap}

/**
  * This trait represents a triples map.
  * A triples map specifies the rules for translating
  *
  * - each row of a database,
  * - each record of a CSV or a TSV data source,
  * - each element of an XML data source,
  * - each object of a JSON data source,
  * - etc...
  *
  * to zero or more RDF triples.
  *
  * Spec: http://rml.io/spec.html#triples-map
  *
  */
trait TriplesMap extends Node {


  /**
    *
    * @return
    */
  def predicateObjectMaps: List[PredicateObjectMap]

  /**
    *
    * @return
    */
  def logicalSource: LogicalSource

  /**
    *
    * @return
    */
  def subjectMap: SubjectMap

  /**
    *
    * @return
    */
  def graphMap: Option[GraphMap]

  /**
    *
    * @return
    */
  def containsParentTriplesMap: Boolean

}

object TriplesMap {

  /**
    *
    * @param predicateObjectMaps
    * @param logicalSource
    * @param subjectMap
    * @param identifier
    * @return
    */
  def apply(predicateObjectMaps: List[PredicateObjectMap],
            logicalSource: LogicalSource,
            subjectMap: SubjectMap,
            identifier: String,
            graphMap: Option[GraphMap] = None
           ): TriplesMap = {

    val triplesMap = StdTriplesMap(predicateObjectMaps,
      logicalSource,
      subjectMap,
      graphMap,
      identifier)

    if (logicalSource.source.isInstanceOf[StreamDataSource]) {
      StdStreamTriplesMap(triplesMap)
    } else {
      triplesMap
    }
  }

}
