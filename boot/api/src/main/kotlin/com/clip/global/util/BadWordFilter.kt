package com.clip.global.util

object BadWordFilter {
    private val badWords: Set<String> by lazy { loadWords("badwords.txt") }
    private val delimiters: List<String> by lazy { loadWords("delimiters.txt").toList() }

    private val badWordPatterns: Map<String, Regex> by lazy {
        badWords.associateWith { word ->
            val patternText = buildPatternText()
            word.toCharArray().joinToString(patternText) { Regex.escape(it.toString()) }
                .toRegex(RegexOption.IGNORE_CASE)
        }
    }

    private fun loadWords(filename: String): Set<String> =
        javaClass.classLoader.getResourceAsStream(filename)!!.bufferedReader().readLines().toSet()

    private fun buildPatternText(): String =
        delimiters.joinToString(separator = "", prefix = "[", postfix = "]*") { Regex.escape(it) }

    fun isBadWord(input: String): Boolean =
        badWordPatterns.values.any { it.containsMatchIn(input) }
}