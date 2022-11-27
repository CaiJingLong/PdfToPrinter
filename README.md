# convert pdf to pwg or pclm

The repo is Kotlin/jvm code

The code is come from: [jipp example][], the library just wrapper sample code.

## To use the code

1. copy code of use [jitpack.io][jitpack]
2. import
```kts
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.github.CaiJingLong:PdfToPrinter:1.0.0")
}
```
3. use code in kotlin

```kt
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
```

[jipp example]: https://github.com/HPInc/jipp/blob/master/sample/jrender/src/main/java/sample/jrender/Main.java
[jitpack]: https://jitpack.io
