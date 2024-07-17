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
package io.rml.framework.util.fileprocessing

import io.rml.framework.Main
import io.rml.framework.core.extractors.NodeCache
import io.rml.framework.core.util.{Format, NQuads, Util}
import io.rml.framework.engine.{NopPostProcessor, PostProcessor}
import io.rml.framework.util.logging.Logger
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import java.io.File

/**
  * Test helper to get generated triples after processing the mapping file
  * containing triple maps.
  *
  */
object TripleGeneratorTestUtil extends TestFilesUtil[(List[String], Format)] {
  implicit val env = ExecutionEnvironment.getExecutionEnvironment
  implicit val senv = StreamExecutionEnvironment.getExecutionEnvironment
  implicit var postProcessor: PostProcessor = new NopPostProcessor


  def apply(postProcessor: PostProcessor) = {
    this.postProcessor = postProcessor
    this
  }


  override def getHelperSpecificFiles(testCaseFolder: String): Array[File] = {
    MappingTestUtil.getHelperSpecificFiles(testCaseFolder)
  }


  /**
   * Process the given mapping file into a DataSet and return a list of string containing triples generated
   * by the mapping process.
   *
   * @param file file containing triple maps
   * @return list of triples generated by processing the mapping file
   */
  def processFile(file: File): (List[String], Format) = {
    try {
      NodeCache.clear();

      val formattedMapping = Util.readMappingFile(file.getCanonicalPath)
      val dataSet = Main.createDataSetFromFormattedMapping(formattedMapping).collect

      // we assume quads, since we don't use a post processor
      val format = NQuads
      val result = if (dataSet.nonEmpty) dataSet.reduce((a, b) => a + "\n" + b) else ""
      Logger.logInfo("Input file: " + file)
      Logger.logInfo("Result from processing: " + result.length)
      if (result.nonEmpty) {
        (result.split('\n').toList, format)
      } else {
        (List(), format)
      }
    } catch {
      case e: Throwable => {
        Logger.logError(s"Processing ${file.getName} FAILED!", e)
        throw e
      }
    }
  }

  /**
   * Process the given mapping file into a DataStream and return a list of strings containing the triples generated
   *
   * @param file file containing the triple maps
   * @return a list of triples generated by processing the mapping file as a stream
   */
  def processFileDataStream(file: File): (List[String], Format) = {
    try {
      NodeCache.clear()

      val format = NQuads
      val formattedMapping = Util.readMappingFile(file.getCanonicalPath)
      val dataStream = Main.createDataStreamFromFormattedMapping(formattedMapping)

      val result = dataStream.executeAndCollect().toList

      if (result.nonEmpty) {
        (result, format)
      } else {
        (List(), format)
      }
    } catch {
      case e: Throwable =>
        Logger.logError(s"Processing ${file.getName} FAILED!", e)
        throw e
    }
  }

}
