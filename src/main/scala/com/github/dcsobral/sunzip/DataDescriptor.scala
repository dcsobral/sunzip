/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 1:03 PM
 */

package com.github.dcsobral.sunzip

import java.nio.ByteBuffer
import ZipConstants._

/** Registro opcional que ocorre após o término dos dados compactados de uma entrada.
  *
  * @param zipFile Arquivo ZIP.
  * @param offset Posição no arquivo desta entrada.
  */
final class DataDescriptor(protected val zipFile: Array[Byte], protected val offset: Int) extends Header {
  private[this] val fieldsOffset = if (temAssinaturaValida) 0 else -4

  def temAssinaturaValida: Boolean = signature == DataDescriptorSignature

  def signature: Int = getInt(DataDescriptorSignatureOffset)

  def crc: Int = getInt(DataDescriptorCRCOffset + fieldsOffset)

  def compressedSize: Int = getInt(DataDescriptorCompressedSizeOffset + fieldsOffset)

  def uncompressedSize: Int = getInt(DataDescriptorUncompressedSizeOffset + fieldsOffset)
}
