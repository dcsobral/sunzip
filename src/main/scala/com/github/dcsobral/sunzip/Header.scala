/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 1:02 PM
 */

package com.github.dcsobral.sunzip

import java.nio.charset.Charset

/** Helpers para manipulação dos headers do formato ZIP. */
private[sunzip] trait Header {
  protected def zipFile: Array[Byte]
  protected def offset: Int

  protected def getShort(posicao: Int): Int = Header getShort (zipFile, posicao + offset)

  protected def getInt(posicao: Int): Int = Header getInt (zipFile, posicao + offset)
}

/** Constantes auxiliares */
object Header {
  val ASCII: Charset = Charset.forName("ASCII")

  def unsig(x: Byte): Int = x.toInt & 0xff

  def getShort(array: Array[Byte], posicao: Int): Int = unsig(array(posicao)) + 0x100 * unsig(array(posicao + 1))

  def getInt(array: Array[Byte], posicao: Int): Int = getShort(array, posicao) + 0x10000 * (getShort(array, posicao + 2))
}
