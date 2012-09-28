/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 5:09 PM
 */

package com.github.dcsobral.sunzip

import java.nio.ByteBuffer

/** Coleção das entradas do diretório central.
  *
  * @param byteBuffer Arquivo ZIP.
  * @param offset Offset do primeiro Central Directory File Header Entry.
  * @param numberOfEntries Número de Central Directory File Header Entries.
  */
final class CentralDirectory(protected val byteBuffer: ByteBuffer,
                             protected val offset: Int,
                             numberOfEntries: Int) extends Header with IndexedSeq[CentralDirectoryEntry] {
  private[this] val entries = IndexedSeq.iterate(new CentralDirectoryEntry(byteBuffer, offset), numberOfEntries)(_.nextCentralDirectoryEntry)

  def apply(idx: Int) = entries(idx)

  def length = numberOfEntries

  override def toString: String = entries mkString "\n"
}
