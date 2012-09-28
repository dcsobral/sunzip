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
  * @param byteBuffer Arquivo ZIP.
  * @param offset Posição no arquivo desta entrada.
  */
final class DataDescriptor(protected val byteBuffer: ByteBuffer, protected val offset: Int) extends Header {
  private[this] val fieldsOffset = if (temAssinaturaValida) 0 else -4

  def temAssinaturaValida = signature == DataDescriptorSignature

  def signature = getInt(DataDescriptorSignatureOffset)
  def signature_=(value: Int) = putInt(DataDescriptorSignatureOffset, value)

  def crc: Int = getInt(DataDescriptorCRCOffset + fieldsOffset)
  def crc_=(value: Int) { putInt(DataDescriptorCRCOffset + fieldsOffset, value) }

  def compressedSize: Int = getInt(DataDescriptorCompressedSizeOffset + fieldsOffset)
  def compressedSize_=(value: Int) { putInt(DataDescriptorCompressedSizeOffset + fieldsOffset, value) }

  def uncompressedSize: Int = getInt(DataDescriptorUncompressedSizeOffset + fieldsOffset)
  def uncompressedSize_=(value: Int) { putInt(DataDescriptorUncompressedSizeOffset + fieldsOffset, value) }
}
