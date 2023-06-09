package io.github.slava0135.doktestidea

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.awt.event.MouseEvent
import java.util.function.Supplier
import org.jetbrains.plugins.gradle.action.GradleExecuteTaskAction

class DoctestLineMarker : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (!isDoctestHeader(element)) {
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
            val workDirectory = project.basePath ?: return
            val path = elt.containingFile.virtualFile.path.removePrefix(workDirectory)
            val document = PsiDocumentManager.getInstance(project).getDocument(elt.containingFile) ?: return
            val line = document.getLineNumber(elt.startOffset) + 1
            val commandLine = "doktest --file $path --line $line"
            GradleExecuteTaskAction.runGradle(project, null, workDirectory, commandLine)
        }
    }
}

private class AccessibleNameProvider : Supplier<String> {
    override fun get() = "TODO"
}