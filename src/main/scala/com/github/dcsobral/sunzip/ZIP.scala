/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 10:56 AM
 */

package com.github.dcsobral.sunzip

import ZipConstants._

/** Arquivo ZIP em Memória.
  *
  * Esta classe manipula um arquivo ZIP fornecido como um array de bytes como se fosse um [[scala.collection.IndexedSeq]]
  * da tupla nome do arquivo, conteúdo (descompactado) do arquivo.
  *
  * Os diversos headers do formato ZIP estão presentes, e as entradas são lidas a partir do End Of Central Directory,
  * conforme requerido pela especificação do formato.
  *
  * Apenar os métodos STORE e DEFLATE (0 e 8) são suportados, e descompressão não é suportada. O nome dos arquivos
  * é lido como ASCII, e o flag de nomes UTF-8 é ignorado.
  *
  * Essa classe não pode ser instanciada diretamente, sendo necessário usar o factory que localiza o início do
  * End of Central Directory Record.
  *
  * @param byteBuffer Byte buffer contendo todo o arquivo ZIP.
  * @param endOfCentralDirectory End Of Central Directory Record do bytebuffer passado.
  */
final class ZIP private(byteBuffer: Array[Byte],
                        val endOfCentralDirectory: EndOfCentralDirectory) extends IndexedSeq[(String, () => Array[Byte])] {

  /** Número de entradas no Central Directory de acordo com o End of Central Directory Record */
  def numberOfEntries: Int = endOfCentralDirectory.numberOfEntries

  /** Coleção de entradas do Central Directory. */
  val centralDirectory: CentralDirectory = endOfCentralDirectory.centralDirectory

  override def iterator: Iterator[(String, () => Array[Byte])] =
    centralDirectory.iterator map (header => header.filename -> (() => header.uncompressedData))

  def length: Int = centralDirectory.size

  def apply(idx: Int): (String, () => Array[Byte]) =
    centralDirectory(idx).filename -> (() => centralDirectory(idx).uncompressedData)

  override def toString(): String = "ZIP:\n" + centralDirectory.toString().lines.zipWithIndex.map {
    case (line, index) => "\t%d: %s" format(index, line)
  }.mkString("\n")
}

/** Factory para objetos ZIP */
object ZIP {
  /** Retorna objeto ZIP se for encontrado um End Of Central Directory, e se o mesmo,
    * e todas as entradas de Central Directory localizadas a partir dele, tiverem
    * assinaturas válidas.
    *
    * @param array Arquivo ZIP.
    * @return Objeto ZIP.
    */
  def apply(array: Array[Byte]): Option[ZIP] = {
    try {
      findEndOfCentralDirectoryHeader(array) map { offset =>
        val endOfCentralDirectoryHeader = new EndOfCentralDirectory(array, offset)
        new ZIP(array, endOfCentralDirectoryHeader)
      } filter (_.centralDirectory.forall(_.temAssinaturaValida))
    } catch {
      case _: Exception => None
    }
  }

  private[this] def findEndOfCentralDirectoryHeader(array: Array[Byte]): Option[Int] = {
    if (array.length < EndOfCentralDirectorySize) None
    else {
      var pos = array.length - EndOfCentralDirectorySize

      while (pos > 0 && Header.getInt(array, pos) != EndOfCentralDirectorySignature) pos -= 1

      if (pos > 0) Some(pos) else None
    }
  }
}
