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

package io.rml.framework.std

import io.rml.framework.core.extractors.ObjectMapExtractor
import io.rml.framework.core.model.Uri
import io.rml.framework.core.model.rdf.{RDFGraph, RDFResource}
import io.rml.framework.core.vocabulary.R2RMLVoc
import org.scalatest.{FunSuite, Matchers}

class StdObjectMapExtractorTest extends FunSuite with Matchers {

  test("testExtract") {
    // ============================================================================================
    // Test info
    // ============================================================================================

    /**
      * Tests the normal flow.
      * Extracts predicate maps.
      */

    // ============================================================================================
    // Test setup
    // ============================================================================================

    // all RDFResource/RDFLiteral instances will be automatically added here
    implicit val graph: RDFGraph = RDFGraph(Some(Uri("#RMLMapping")))

    val resource =
      RDFResource("#PredicateObjectMap")
        .addProperty(R2RMLVoc.Property.OBJECTMAP,
          RDFResource("#ObjectMap")
            .addProperty(R2RMLVoc.Property.CONSTANT,
              RDFResource("#Object")))

        .addProperty(R2RMLVoc.Property.OBJECT,
          RDFResource("#Object"))

    // ============================================================================================
    // Test execution
    // ============================================================================================

    val objectMaps = ObjectMapExtractor().extract(resource)

    // ============================================================================================
    // Test verification
    // ============================================================================================

    objectMaps.length shouldNot be (0)

    objectMaps
      .exists(objectMap => objectMap.identifier == "") should be (true)

    objectMaps
      .exists(objectMap =>
          objectMap.constant.isDefined
          && objectMap.identifier == "#ObjectMap") should be (true)
  }

}
