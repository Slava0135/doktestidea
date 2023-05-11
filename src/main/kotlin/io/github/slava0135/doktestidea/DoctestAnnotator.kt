package io.github.slava0135.doktestidea

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.idea.highlighter.KotlinSyntaxHighlighterFactory

class DoctestAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (isDoctest(element)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(DefaultLanguageHighlighterColors.IDENTIFIER)
                .create()
            val highlighter = KotlinSyntaxHighlighterFactory.getSyntaxHighlighter(KotlinLanguage.INSTANCE, null, null)
            val lexer = highlighter.highlightingLexer
            lexer.start(element.text)
            val offset = element.textRange.startOffset
            while (lexer.tokenType != null) {
                val range = TextRange.from(offset + lexer.tokenStart, lexer.tokenText.length)
                val tokenHighlights = highlighter.getTokenHighlights(lexer.tokenType)
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range)
                    .apply {
                        tokenHighlights.forEach {
                            textAttributes(it)
                        }
                    }
                    .create()
                lexer.advance()
            }
        }
    }
}
