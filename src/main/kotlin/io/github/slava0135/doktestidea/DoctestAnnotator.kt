package io.github.slava0135.doktestidea

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

const val CODE_BLOCK_PREFIX = "```kotlin "
const val DOCTEST_PREFIX = "doctest"
const val DOCTEST_SEP = ':'

val DOCTEST_OPTIONS = mapOf(
    "run" to "Run this code as if it was put in 'main' function",
    "norun" to "Compile this code as if it was put in 'main' function",
    "nomain" to "Compile this code as is"
)

class DoctestAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.elementType.toString() != "KDOC_TEXT") {
            return
        }
        if (!element.text.trimStart().startsWith(CODE_BLOCK_PREFIX, ignoreCase = true)) {
            return
        }

        val prefixIndex = element.text.indexOf(DOCTEST_PREFIX, ignoreCase = true)
        if (prefixIndex < 0) {
            return
        }
        val prefixRange = TextRange.from(element.textRange.startOffset + prefixIndex, DOCTEST_PREFIX.length)
        holder.newAnnotation(HighlightSeverity.INFORMATION, "Mark this code block as documentation test")
            .range(prefixRange)
            .textAttributes(DefaultLanguageHighlighterColors.KEYWORD)
            .create()

        val sepIndex = prefixIndex + DOCTEST_PREFIX.length
        if (element.text.length <= sepIndex) {
            return
        }
        if (element.text[sepIndex] != DOCTEST_SEP && !element.text[sepIndex].isWhitespace()) {
            val unknownRange = TextRange.from(element.textRange.startOffset + sepIndex, 1)
            holder.newAnnotation(HighlightSeverity.WARNING, "Unknown symbol, expected ' $DOCTEST_SEP ' or next word/line")
                .range(unknownRange)
                .create()
            return
        }
        val sepRange = TextRange.from(element.textRange.startOffset + sepIndex, 1)
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(sepRange)
            .textAttributes(DefaultLanguageHighlighterColors.SEMICOLON)
            .create()

        val optIndex = sepIndex + 1
        if (element.text.length <= optIndex) {
            holder.newAnnotation(HighlightSeverity.WARNING, "No option provided, expected one of following: ${DOCTEST_OPTIONS.keys}")
                .range(sepRange)
                .create()
            return
        }

        for ((opt, msg) in DOCTEST_OPTIONS) {
            if (element.text.startsWith(opt, optIndex, ignoreCase = true)) {
                val optRange = TextRange.from(element.textRange.startOffset + optIndex, opt.length)
                holder.newAnnotation(HighlightSeverity.INFORMATION, msg)
                    .range(optRange)
                    .textAttributes(DefaultLanguageHighlighterColors.KEYWORD)
                    .create()
                return
            }
        }

        val unknownRange = TextRange.from(element.textRange.startOffset + optIndex, element.textRange.length - optIndex)
        holder.newAnnotation(HighlightSeverity.WARNING, "Unknown option, expected one of following: ${DOCTEST_OPTIONS.keys}")
            .range(unknownRange)
            .create()
    }
}
