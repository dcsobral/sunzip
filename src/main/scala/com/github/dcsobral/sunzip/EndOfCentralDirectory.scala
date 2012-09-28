/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 3:59 PM
 */

package com.github.dcsobral.sunzip

import ZipConstants._

/** Registro final do arquivo ZIP, a partir do qual todas as demais entradas são encontradas.
  *
  * O End Of Central Directory ocorre no fim do arquivo, com tamanho variável (pois pode conter
  * um comentário). A partir dele, pode-se encontrar o central directory, saber quantas entradas
  * existem no central directory, e, a partir dessas entradas, localizar as entradas locais que
  * precedem cada arquivo compactado.
  *
  * @param zipFile Arquivo ZIP.
  * @param offset Offset onde se encontra o End Of Central Directory Record.
  */
final class EndOfCentralDirectory(protected val zipFile: Array[Byte], protected val offset: Int) extends Header {
  def temAssinaturaValida: Boolean = signature == EndOfCentralDirectorySignature

  def signature: Int = getInt(EndOfCentralDirectorySignatureOffset)

  def numberOfDisks: Int = getShort(EndOfCentralDirectoryDiskNumberOffset)

  def centralDirectoryStartingDisk: Int = getShort(EndOfCentralDirectoryStartingDiskOfCentralDirectoryOffset)

  def entriesOnThisDisk: Int = getShort(EndOfCentralDirectoryEntriesOnDiskOffset)

  def numberOfEntries: Int = getShort(EndOfCentralDirectoryNumberOfEntriesOffset)

  def sizeOfCentralDirectory: Int = getInt(EndOfCentralDirectorySizeOfCentralDirectoryOffset)

  def offsetOfCentralDirectory: Int = getInt(EndOfCentralDirectoryOffsetOfCentralDirectoryOffset)

  def commentLength: Int = getInt(EndOfCentralDirectoryCommentLengthOffset)

  /** Coleção de entradas do Central Directory. */
  def centralDirectory: CentralDirectory = new CentralDirectory(zipFile, offsetOfCentralDirectory, numberOfEntries)
}
