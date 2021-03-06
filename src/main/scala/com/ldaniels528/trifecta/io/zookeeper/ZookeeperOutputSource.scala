package com.ldaniels528.trifecta.io.zookeeper

import com.ldaniels528.trifecta.io.avro.AvroDecoder
import com.ldaniels528.trifecta.io.{KeyAndMessage, OutputSource}
import com.ldaniels528.trifecta.messages.MessageDecoder

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
 * Zookeeper Output Source
 * @author Lawrence Daniels <lawrence.daniels@gmail.com>
 */
class ZookeeperOutputSource(zk: ZKProxy, rootPath: String) extends OutputSource {

  /**
   * Returns the binary encoding
   * @return the binary encoding
   */
  val encoding: String = "UTF8"

  /**
   * Writes the given key and decoded message to the underlying stream
   * @param data the given key and message
   * @return the response value
   */
  override def write(data: KeyAndMessage, decoder: Option[MessageDecoder[_]])(implicit ec: ExecutionContext) {
    decoder match {
      case Some(av: AvroDecoder) =>
        av.decode(data.message) match {
          case Success(record) =>
            val path = s"$rootPath/${new String(data.key, encoding)}"
            zk.create(path, data.message)
            ()
          case Failure(e) =>
            throw new IllegalStateException(e.getMessage, e)
        }
      case Some(unhandled) =>
        throw new IllegalStateException(s"Unhandled decoder '$unhandled'")
      case None =>
        val path = s"$rootPath/${new String(data.key, encoding)}"
        zk.create(path, data.message)
        ()
    }
  }

  override def close() = ()

}
