package top.kikt.pdf

import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.io.IOException

internal class ConverterKtTest {

    @Test
    fun convertPdfTest() {
        for (outputFormat in OutputFormat.values()) {
            val inputStream =
                FileInputStream("testFiles/2022-11-26.pdf") ?: throw IOException("The inputStream cannot open")
            inputStream.use {
                convertPdf(inputStream, outputFormat) {
                    val bytes = it.toByteArray()
                    println("$outputFormat bytes count: ${bytes.count()}")
                }
            }
        }
    }

}