/**
 * Created with IntelliJ IDEA.
 * User: dcs
 * Date: 9/27/12
 * Time: 3:26 PM
 */

package com.github.dcsobral.sunzip

/** Constantes auxiliares para o tratamento do formato ZIP. */
object ZipConstants {
  final val LocalEntrySignature = 0x04034b50
  final val DataDescriptorSignature = 0x08074b50
  final val CentralDirectoryEntrySignature = 0x02014b50
  final val EndOfCentralDirectorySignature = 0x06054b50

  final val LocalEntryFixedSize = 30
  final val DataDescriptorSize = 16
  final val CentralDirectoryEntryFixedSize = 46
  final val EndOfCentralDirectorySize = 22

  final val LocalEntrySignatureOffset = 0
  final val LocalEntryVersionOffset = 4
  final val LocalEntryFlagsOffset = 6
  final val LocalEntryMethodOffset = 8
  final val LocalEntryDateTimeOffset = 10
  final val LocalEntryCRCOffset = 14
  final val LocalEntryCompressedSizeOffset = 18
  final val LocalEntryUncompressedSizeOffset = 22
  final val LocalEntryFileNameSizeOffset = 26
  final val LocalEntryExtraFieldSizeOffset = 28

  final val DataDescriptorSignatureOffset = 0
  final val DataDescriptorCRCOffset = 4
  final val DataDescriptorCompressedSizeOffset = 8
  final val DataDescriptorUncompressedSizeOffset = 12

  final val CentralDirectoryEntrySignatureOffset = 0
  final val CentralDirectoryEntryVersionMadeByOffset = 4
  final val CentralDirectoryEntryVersionRequiredOffset = 6
  final val CentralDirectoryEntryFlagsOffset = 8
  final val CentralDirectoryEntryMethodOffset = 10
  final val CentralDirectoryEntryDateTimeOffset = 12
  final val CentralDirectoryEntryCRCOffset = 16
  final val CentralDirectoryEntryCompressedSizeOffset = 20
  final val CentralDirectoryEntryUncompressedSizeOffset = 24
  final val CentralDirectoryEntryFileNameSizeOffset = 28
  final val CentralDirectoryEntryExtraFieldsSizeOffset = 30
  final val CentralDirectoryEntryFileCommentLengthOffset = 32
  final val CentralDirectoryEntryStartingDiskOffset = 34
  final val CentralDirectoryEntryInternalAttributesOffset = 36
  final val CentralDirectoryEntryExternalAttributesOffset = 38
  final val CentralDirectoryEntryRelativeOffsetOfLocalFileHeaderOffset = 42

  final val EndOfCentralDirectorySignatureOffset = 0
  final val EndOfCentralDirectoryDiskNumberOffset = 4
  final val EndOfCentralDirectoryStartingDiskOfCentralDirectoryOffset = 6
  final val EndOfCentralDirectoryEntriesOnDiskOffset = 8
  final val EndOfCentralDirectoryNumberOfEntriesOffset = 10
  final val EndOfCentralDirectorySizeOfCentralDirectoryOffset = 12
  final val EndOfCentralDirectoryOffsetOfCentralDirectoryOffset = 16
  final val EndOfCentralDirectoryCommentLengthOffset = 20

  final val flagPasswordRequired = 0x0001
  final val flag8KSlidingWindow = 0x0002
  final val flag3ShannonFano = 0x0004
  final val flagMaximum = 0x0002
  final val flagFast = 0x0004
  final val flagSuperFast = 0x0006
  final val flagEOSMarker = 0x0002
  final val flagCRCAndSizeUnavailable = 0x0008
  final val flagCompressedPatchedData = 0x0020
  final val flagStrongEncryption = 0x0040
  final val flagUTF8 = 0x0800
  final val flagMaskedData = 0x2000

  final val methodStored = 0
  final val methodShrunk = 1
  final val methodReducedFactor1 = 2
  final val methodReducedFactor2 = 3
  final val methodReducedFactor3 = 4
  final val methodReducedFactor4 = 5
  final val methodImploded = 6
  final val methodTokenized = 7
  final val methodDeflated = 8
  final val methodDeflated64 = 9
  final val methodBZIP2 = 12
  final val methodLZMA = 14
  final val methodTerse = 18
  final val methodLZ77 = 19
  final val methodWavPack = 97
  final val methodPPMd = 98

  /** Maps compression methods to names. */
  val methodName = Map(
    methodStored -> "stored",
    methodShrunk -> "shrunk",
    methodReducedFactor1 -> "reduced factor 1",
    methodReducedFactor2 -> "reduced factor 2",
    methodReducedFactor3 -> "reduced factor 3",
    methodReducedFactor4 -> "reduced factor 4",
    methodImploded -> "imploded",
    methodTokenized -> "tokenized",
    methodDeflated -> "deflated",
    methodDeflated64 -> "deflated64",
    methodBZIP2 -> "bzip2",
    methodLZMA -> "LZMA (EFS)",
    methodTerse -> "Terse",
    methodLZ77 -> "LZ77 (PFS)",
    methodWavPack -> "wavpack",
    methodPPMd -> "PPMd"
  )
}
