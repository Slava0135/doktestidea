package com.github.slava0135.doktestidea

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.icons.AllIcons
import com.intellij.psi.util.elementType
import java.util.function.Supplier

class LineMarker : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element.elementType.toString() != "KDOC_TEXT") {
            return null
        }
        if (!element.text.trimStart().startsWith("```kotlin doctest")) {
            return null
        }
        return LineMarkerInfo(
            element,
            element.textRange,
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