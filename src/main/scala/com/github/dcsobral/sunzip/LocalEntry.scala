/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 1:04 PM
 */

package com.github.dcsobral.sunzip

import ZipConstants._

/** Entrada local de um arquivo comprimido.
  *
  * As entradas locais podem não conter o tamanho do arquivo comprimido nem seu CRC.
  *
  * @param zipFile Arquivo ZIP.
  * @param offset Posição no arquivo desta entrada.
  */
final class LocalEntry(protected val zipFile: Array[Byte], protected val offset: Int) extends Header {
  def temAssinaturaValida: Boolean = signature == LocalEntrySignature
  def temTamanhoConhecido: Boolean = (flags & flagCRCAndSizeUnavailable) == 0

  def signature: Int = getInt(LocalEntrySignatureOffset)

  def version: Int = getShort(LocalEntryVersionOffset)

  def flags: Int = getShort(LocalEntryFlagsOffset)

  def method: Int = getShort(LocalEntryMethodOffset)

  def time: Int = getInt(LocalEntryDateTimeOffset)

  def crc: Int = getInt(LocalEntryCRCOffset)

  def compressedSize: Int = getInt(LocalEntryCompressedSizeOffset)

  def uncompressedSize: Int = getInt(LocalEntryUncompressedSizeOffset)

  def fileNameLength: Int = getShort(LocalEntryFileNameSizeOffset)

  def extraFieldLength: Int = getShort(LocalEntryExtraFieldSizeOffset)

  // TODO: tratar nomes de arquivo em UTF-8
  // TODO: permitir especificar locale ao extrair o nome do arquivo
  /** Retorna o nome do arquivo decodificado em ASCII. */
  def filename: String = new String(zipFile, LocalEntryFixedSize + offset, fileNameLength, Header.ASCII)

  val size: Int = LocalEntryFixedSize + fileNameLength + extraFieldLength
  def length: Int = size

  /** Posição no arquivo dos dados compactados. */
  def dataOffset: Int = offset + size

  override def toString: String = filename+sizeToString

  private[this] def sizeToString: String =
    if (temTamanhoConhecido) "\t%8d (%s %d)".format(uncompressedSize, methodName(method), compressedSize)
    else "size unknown (%s)".format(methodName(method))
}
