package top.kikt.pdf

enum class OutputFormat(val title: String) {
    PWG_RASTER("pwg"), PCLM("PCLm");

    companion object {
        fun toOutputFormat(formatName: String): OutputFormat {
            for (format in values()) {
                if (format.title.equals(formatName, ignoreCase = true)) {
                    return format
                }
            }
            throw IllegalArgumentException("Output format $formatName is invalid")
        }
    }
}