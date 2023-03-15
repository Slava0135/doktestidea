package com.github.slava0135.doktestidea

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.icons.AllIcons
import java.util.function.Supplier

class LineMarker : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element !is LeafPsiElement || element.elementType.toString() != "KDOC_CODE_BLOCK_TEXT") {
            return null
        }
        return LineMarkerInfo(
            element,
            element.getTextRange(),
            AllIcons.Actions.Execute,
            null,
            null,
            GutterIconRenderer.Alignment.CENTER,
            AccessibleNameProvider()
        )
    }
}

private class AccessibleNameProvider : Supplier<String> {
    override fun get() = "TODO"
}