package io.github.slava0135.doktestidea

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class DoctestAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!isDoctestHeader(element)) {
            return
        }

        val prefixIndex = element.text.indexOf(DOCTEST_PREFIX, ignoreCase = true)
        val prefixRange = TextRange.from(element.textRange.startOffset + prefixIndex, DOCTEST_PREFIX.length)
        holder.newAnnotation(HighlightSeverity.INFORMATION, "Mark this code block as documentation test")
            .range(prefixRange)
            .textAttributes(DefaultLanguageHighlighterColors.KEYWORD)
            .create()

        val sepIndex = prefixIndex + DOCTEST_PREFIX.length
        if (element.text.length <= sepIndex || element.text[sepIndex].isWhitespace()) {
            return
        }
        if (element.text[sepIndex] != DOCTEST_SEP) {
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
