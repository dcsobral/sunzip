/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 3:59 PM
 */

package com.github.dcsobral.sunzip

import java.nio.ByteBuffer
import ZipConstants._

/** Registro final do arquivo ZIP, a partir do qual todas as demais entradas são encontradas.
  *
  * O End Of Central Directory ocorre no fim do arquivo, com tamanho variável (pois pode conter
  * um comentário). A partir dele, pode-se encontrar o central directory, saber quantas entradas
  * existem no central directory, e, a partir dessas entradas, localizar as entradas locais que
  * precedem cada arquivo compactado.
  *
  * @param byteBuffer Arquivo ZIP.
  * @param offset Offset onde se encontra o End Of Central Directory Record.
  */
final class EndOfCentralDirectory(protected val byteBuffer: ByteBuffer, protected val offset: Int) extends Header {
  def temAssinaturaValida: Boolean = signature == EndOfCentralDirectorySignature

  def signature: Int = getInt(EndOfCentralDirectorySignatureOffset)
  def signature_=(value: Int) { putInt(EndOfCentralDirectorySignatureOffset, value) }

  def numberOfDisks: Int = getShort(EndOfCentralDirectoryDiskNumberOffset)
  def numberOfDisks_=(value: Int) { putShort(EndOfCentralDirectoryDiskNumberOffset, value) }

  def centralDirectoryStartingDisk: Int = getShort(EndOfCentralDirectoryStartingDiskOfCentralDirectoryOffset)
  def centralDirectoryStartingDisk_=(value: Int) {
    putShort(EndOfCentralDirectoryStartingDiskOfCentralDirectoryOffset, value)
  }

  def entriesOnThisDisk: Int = getShort(EndOfCentralDirectoryEntriesOnDiskOffset)
  def entriesOnThisDisk_=(value: Int) { putShort(EndOfCentralDirectoryEntriesOnDiskOffset, value) }

  def numberOfEntries: Int = getShort(EndOfCentralDirectoryEntriesOffset)
  def numberOfEntries_=(value: Int) { putShort(EndOfCentralDirectoryEntriesOffset, value) }

  def sizeOfCentralDirectory: Int = getInt(EndOfCentralDirectorySizeOfCentralDirectoryOffset)
  def sizeOfCentralDirectory_=(value: Int) { putInt(EndOfCentralDirectorySizeOfCentralDirectoryOffset, value) }

  def offsetOfCentralDirectory: Int = getInt(EndOfCentralDirectoryOffsetOfCentralDirectoryOffset)
  def offsetOfCentralDirectory_=(value: Int) { putInt(EndOfCentralDirectoryOffsetOfCentralDirectoryOffset, value) }

  def commentLength: Int = getInt(EndOfCentralDirectoryCommentLengthOffset)
  def commentLength_=(value: Int) = { putInt(EndOfCentralDirectoryCommentLengthOffset, value) }

  /** Coleção de entradas do Central Directory. */
  def centralDirectory: CentralDirectory = new CentralDirectory(byteBuffer, offsetOfCentralDirectory, numberOfEntries)
}
