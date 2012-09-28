/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 1:07 PM
 */

package com.github.dcsobral.sunzip

import java.nio.ByteBuffer
import ZipConstants._
import java.util.Arrays
import java.util.zip.Inflater

/** Entrada do diretório central.
  *
  * Essa entrada contém sempre o tamanho compactado e não compactado do arquivo, assim como
  * o CRC, e uma indicação de onde encontrar a entrada local. O tamanho do arquivo e CRC não
  * são obrigratórios nas entradas locais. Por isso, o método de extração do arquivo encontra-se
  * nessa classe.
  *
  * @param byteBuffer Arquivo ZIP.
  * @param offset Posição onde se encontra este registro.
  */
final class CentralDirectoryEntry(protected val byteBuffer: ByteBuffer, protected val offset: Int) extends Header {
  def temAssinaturaValida: Boolean = signature == CentralDirectoryEntrySignature

  def signature: Int = getInt(CentralDirectoryEntrySignatureOffset)
  def signature_=(value: Int) { putInt(CentralDirectoryEntrySignatureOffset, value) }

  def versionMadeBy: Int = getShort(CentralDirectoryEntryVersionMadeByOffset)
  def versionMadeBy_=(value: Int) = { putShort(CentralDirectoryEntryVersionMadeByOffset, value) }

  def versionNeededToExtract: Int = getShort(CentralDirectoryEntryVersionRequiredOffset)
  def versionNeededToExtract_=(value: Int) { putShort(CentralDirectoryEntryVersionRequiredOffset, value) }

  def flags: Int = getShort(CentralDirectoryEntryFlagsOffset)
  def flags_=(value: Int) { putShort(CentralDirectoryEntryFlagsOffset, value) }

  def method: Int = getShort(CentralDirectoryEntryMethodOffset)
  def method_=(value: Int) { putShort(CentralDirectoryEntryMethodOffset, value) }

  def time: Int = getInt(CentralDirectoryEntryDateTimeOffset)
  def time_=(value: Int) { putInt(CentralDirectoryEntryDateTimeOffset, value) }

  def crc: Int = getInt(CentralDirectoryEntryCRCOffset)
  def crc_=(value: Int) { putInt(CentralDirectoryEntryCRCOffset, value) }

  def compressedSize: Int = getInt(CentralDirectoryEntryCompressedSizeOffset)
  def compressedSize_=(value: Int) { putInt(CentralDirectoryEntryCompressedSizeOffset, value) }

  def uncompressedSize: Int = getInt(CentralDirectoryEntryUncompressedSizeOffset)
  def uncompressedSize_=(value: Int) { putInt(CentralDirectoryEntryUncompressedSizeOffset, value) }

  def fileNameLength: Int = getShort(CentralDirectoryEntryFileNameSizeOffset)
  def fileNameLength_=(value: Int) { putShort(CentralDirectoryEntryFileNameSizeOffset, value) }

  def extraFieldLength: Int = getShort(CentralDirectoryEntryExtraFieldsSizeOffset)
  def extraFieldLength_=(value: Int) { putShort(CentralDirectoryEntryExtraFieldsSizeOffset, value) }

  def fileCommentLength: Int = getShort(CentralDirectoryEntryFileCommentLengthOffset)
  def fileCommentLength_=(value: Int) { putShort(CentralDirectoryEntryFileCommentLengthOffset, value) }

  def startingDisk: Int = getShort(CentralDirectoryEntryStartingDiskOffset)
  def startingDisk_=(value: Int) { putShort(CentralDirectoryEntryStartingDiskOffset, value) }

  def internalFileAttributes: Int = getShort(CentralDirectoryEntryInternalAttributesOffset)
  def internalFileAttribtues_=(value: Int) { putShort(CentralDirectoryEntryInternalAttributesOffset, value) }

  def externalFileAttributes: Int = getInt(CentralDirectoryEntryExternalAttributesOffset)
  def externalFileAttributes_=(value: Int) { putInt(CentralDirectoryEntryExternalAttributesOffset, value) }

  def relativeOffset: Int = getInt(CentralDirectoryEntryRelativeOffsetOfLocalFileHeaderOffset)
  def relativeOffset_=(value: Int) { putInt(CentralDirectoryEntryRelativeOffsetOfLocalFileHeaderOffset, value) }

  // TODO: tratar nomes de arquivo em UTF-8
  // TODO: permitir especificar locale ao extrair o nome do arquivo
  /** Retorna o nome do arquivo decodificado em ASCII. */
  def filename = new String(byteBuffer.array(), CentralDirectoryEntryFixedSize + offset, fileNameLength, Header.ASCII)

  val size: Int = CentralDirectoryEntryFixedSize + fileNameLength + extraFieldLength + fileCommentLength
  def length: Int = size

  /** Retorna a próxima entrada. */
  def nextCentralDirectoryEntry: CentralDirectoryEntry = new CentralDirectoryEntry(byteBuffer, offset + size)

  /** Retorna a entrada local do arquivo. */
  def localEntry: LocalEntry = new LocalEntry(byteBuffer, relativeOffset)

  /** Retorna o arquivo descompactado.
    *
    * Suporta apenas os métodos STORED (0) e DEFLATE (8).
    */
  def uncompressedData: Array[Byte] = method match {
    case `methodStored` =>
      Arrays.copyOfRange(byteBuffer.array(), localEntry.dataOffset, localEntry.dataOffset + uncompressedSize)

    case `methodDeflated` =>
      val inflater = new Inflater(true)
      val data = new Array[Byte](uncompressedSize)
      var outputOffset = 0

      inflater.setInput(byteBuffer.array(), localEntry.dataOffset, compressedSize)
      while (!inflater.finished() && !inflater.needsDictionary()) {
        val bytesWritten = inflater.inflate(data, outputOffset, data.length - outputOffset)
        outputOffset += bytesWritten
      }
      inflater.end()

      data
  }

  override def toString: String = filename+"\t%8d (%s %d)".format(uncompressedSize, methodName(method), compressedSize)
}
