package top.kikt.pdf

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun convertPdf(
    inputStream: InputStream,
    outputFormat: OutputFormat = OutputFormat.PWG_RASTER,
    action: (outputStream: ByteArrayOutputStream) -> Unit = {}
) {
    val outputStream = PdfConverter.convertPdf(inputStream, outputFormat)
    action(outputStream)
}