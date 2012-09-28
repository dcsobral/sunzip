/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 1:02 PM
 */

package com.github.dcsobral.sunzip

import java.nio.ByteBuffer
import java.nio.charset.Charset

/** Helpers para manipulação dos headers do formato ZIP. */
private[sunzip] trait Header {
  protected def byteBuffer: ByteBuffer
  protected def offset: Int

  protected def getShort(posicao: Int): Int = (byteBuffer getShort posicao + offset).toInt
  protected def putShort(posicao: Int, value: Int) { byteBuffer putShort (posicao + offset, value.toShort) }

  protected def getInt(posicao: Int): Int = byteBuffer getInt posicao + offset
  protected def putInt(posicao: Int, value: Int) { byteBuffer putInt (posicao + offset, value) }
}

object Header {
  val ASCII: Charset = Charset.forName("ASCII")
}
