package top.kikt.pdf

import com.hp.jipp.model.MediaSource
import com.hp.jipp.model.Sides
import com.hp.jipp.pdl.ColorSpace
import com.hp.jipp.pdl.OutputSettings
import com.hp.jipp.pdl.RenderableDocument
import com.hp.jipp.pdl.RenderablePage
import com.hp.jipp.pdl.pclm.PclmSettings
import com.hp.jipp.pdl.pclm.PclmWriter
import com.hp.jipp.pdl.pwg.PwgSettings
import com.hp.jipp.pdl.pwg.PwgWriter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.*
import java.util.*

internal object PdfConverter {
    const val dpi = 300
    private val IMAGE_TYPE = ImageType.RGB
    private const val RED_COEFFICIENT = 0.2126
    private const val GREEN_COEFFICIENT = 0.7512
    private const val BLUE_COEFFICIENT = 0.0722

    @Throws(IOException::class)
    @JvmStatic
    fun convertPdf(inputStream: InputStream, outputFormat: OutputFormat): ByteArrayOutputStream {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val pdfInputStream: InputStream = BufferedInputStream(inputStream)
        val outputStream: OutputStream = BufferedOutputStream(byteArrayOutputStream)
        val colorSpace = convertImageTypeToColorSpace(IMAGE_TYPE)
        PDDocument.load(pdfInputStream).use { document ->
            val pdfRenderer = PDFRenderer(document)
            val pages = document.pages
            val renderablePages: MutableList<RenderablePage> = ArrayList()
            for (pageIndex in 0 until pages.count) {
                val image = pdfRenderer.renderImageWithDPI(pageIndex, dpi.toFloat(), IMAGE_TYPE)
                val width = image.width
                val height = image.height
                val renderablePage: RenderablePage = object : RenderablePage(width, height) {
                    override fun render(yOffset: Int, swathHeight: Int, colorSpace: ColorSpace, byteArray: ByteArray) {
                        var red: Int
                        var green: Int
                        var blue: Int
                        var rgb: Int
                        var byteIndex = 0
                        for (y in yOffset until yOffset + swathHeight) {
                            for (x in 0 until width) {
                                rgb = image.getRGB(x, y)
                                red = rgb shr 16 and 0xFF
                                green = rgb shr 8 and 0xFF
                                blue = rgb and 0xFF
                                if (colorSpace == ColorSpace.Grayscale) {
                                    byteArray[byteIndex++] =
                                        (RED_COEFFICIENT * red + GREEN_COEFFICIENT * green + BLUE_COEFFICIENT * blue).toInt()
                                            .toByte()
                                } else {
                                    byteArray[byteIndex++] = red.toByte()
                                    byteArray[byteIndex++] = green.toByte()
                                    byteArray[byteIndex++] = blue.toByte()
                                }
                            }
                        }
                    }
                }
                renderablePages.add(renderablePage)
            }
            val renderableDocument: RenderableDocument = object : RenderableDocument() {

                override val dpi: Int
                    get() = this@PdfConverter.dpi

                override fun iterator(): Iterator<RenderablePage> {
                    return renderablePages.iterator()
                }
            }
            when (outputFormat) {
                OutputFormat.PCLM -> saveRenderableDocumentAsPCLm(renderableDocument, colorSpace, outputStream)
                OutputFormat.PWG_RASTER -> saveRenderableDocumentAsPWG(renderableDocument, colorSpace, outputStream)
            }
        }

        return byteArrayOutputStream
    }

    @Throws(IOException::class)
    private fun saveRenderableDocumentAsPCLm(
        renderableDocument: RenderableDocument,
        colorSpace: ColorSpace, outputStream: OutputStream
    ) {
        val outputSettings = OutputSettings(colorSpace, Sides.oneSided, MediaSource.auto, null, false)
        val caps = PclmSettings(outputSettings, 32)
//        val caps = PclmSettings(outputSettings, 64)
        val writer = PclmWriter(outputStream, caps)
        writer.write(renderableDocument)
        writer.close()
    }

    @Throws(IOException::class)
    private fun saveRenderableDocumentAsPWG(
        renderableDocument: RenderableDocument,
        colorSpace: ColorSpace, outputStream: OutputStream
    ) {
        val outputSettings = OutputSettings(colorSpace, Sides.oneSided, MediaSource.auto, null, false)
        val caps = PwgSettings(outputSettings)
        val writer = PwgWriter(outputStream, caps)
        writer.write(renderableDocument)
        writer.close()
    }

    private fun convertImageTypeToColorSpace(imageType: ImageType): ColorSpace {
        return when (imageType) {
            ImageType.BINARY, ImageType.GRAY -> ColorSpace.Grayscale
            else -> ColorSpace.Rgb
        }
    }


}