/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 1:04 PM
 */

package com.github.dcsobral.sunzip

import java.nio.ByteBuffer
import ZipConstants._

/** Entrada local de um arquivo comprimido.
  *
  * As entradas locais podem não conter o tamanho do arquivo comprimido nem seu CRC.
  *
  * @param byteBuffer Arquivo ZIP.
  * @param offset Posição no arquivo desta entrada.
  */
final class LocalEntry(protected val byteBuffer: ByteBuffer, protected val offset: Int) extends Header {
  def temAssinaturaValida: Boolean = signature == LocalEntrySignature
  def temTamanhoConhecido: Boolean = (flags & flagCRCAndSizeUnavailable) == 0

  def signature: Int = getInt(LocalEntrySignatureOffset)
  def signature_=(value: Int) { putInt(LocalEntrySignatureOffset, value) }

  def version: Int = getShort(LocalEntryVersionOffset)
  def version_=(value: Int) { putShort(LocalEntryVersionOffset, value) }

  def flags: Int = getShort(LocalEntryFlagsOffset)
  def flags_=(value: Int) { putShort(LocalEntryFlagsOffset, value) }

  def method: Int = getShort(LocalEntryMethodOffset)
  def method_=(value: Int) { putShort(LocalEntryMethodOffset, value) }

  def time: Int = getInt(LocalEntryDateTimeOffset)
  def time_=(value: Int) { putInt(LocalEntryDateTimeOffset, value) }

  def crc: Int = getInt(LocalEntryCRCOffset)
  def crc_=(value: Int) { putInt(LocalEntryCRCOffset, value) }

  def compressedSize: Int = getInt(LocalEntryCompressedSizeOffset)
  def compressedSize_=(value: Int) { putInt(LocalEntryCompressedSizeOffset, value) }

  def uncompressedSize: Int = getInt(LocalEntryUncompressedSizeOffset)
  def uncompressedSize_=(value: Int) { putInt(LocalEntryUncompressedSizeOffset, value) }

  def fileNameLength: Int = getShort(LocalEntryFileNameSizeOffset)
  def fileNameLength_=(value: Int) { putShort(LocalEntryFileNameSizeOffset, value) }

  def extraFieldLength: Int = getShort(LocalEntryExtraFieldSizeOffset)
  def extraFieldLength_=(value: Int) { putShort(LocalEntryExtraFieldSizeOffset, value) }

  // TODO: tratar nomes de arquivo em UTF-8
  // TODO: permitir especificar locale ao extrair o nome do arquivo
  /** Retorna o nome do arquivo decodificado em ASCII. */
  def filename = new String(byteBuffer.array(), LocalEntryFixedSize + offset, fileNameLength, Header.ASCII)

  val size: Int = LocalEntryFixedSize + fileNameLength + extraFieldLength
  def length: Int = size

  /** Posição no arquivo dos dados compactados. */
  def dataOffset = offset + size

  override def toString: String = filename+sizeToString

  private[this] def sizeToString: String =
    if (temTamanhoConhecido) "\t%8d (%s %d)".format(uncompressedSize, methodName(method), compressedSize)
    else "size unknown (%s)".format(methodName(method))
}
