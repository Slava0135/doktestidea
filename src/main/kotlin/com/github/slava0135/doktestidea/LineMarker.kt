package com.github.slava0135.doktestidea

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.idea.core.util.getLineNumber
import java.awt.event.MouseEvent
import java.util.function.Supplier
import org.jetbrains.plugins.gradle.action.GradleExecuteTaskAction

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
            NavigationHandler(),
            GutterIconRenderer.Alignment.CENTER,
            AccessibleNameProvider()
        )
    }
}

private class NavigationHandler : GutterIconNavigationHandler<PsiElement> {
    override fun navigate(e: MouseEvent, elt: PsiElement) {
        if (e.id == MouseEvent.MOUSE_RELEASED) {
            val project = elt.containingFile.project
            val path = elt.containingFile.virtualFile.path
//            val line = elt.getLineNumber()
            val commandLine = "doktest --file $path"// --line $line"
            val workDirectory = project.basePath ?: return
            GradleExecuteTaskAction.runGradle(project, null, workDirectory, commandLine)
        }
    }
}

private class AccessibleNameProvider : Supplier<String> {
    override fun get() = "TODO"
}