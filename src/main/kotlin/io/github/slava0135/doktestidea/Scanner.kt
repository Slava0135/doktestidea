package io.github.slava0135.doktestidea

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

fun isDoctestHeader(context: PsiElement): Boolean {
    if (context.elementType.toString() != "KDOC_TEXT") {
        return false
    }
    return context.text.trimStart().startsWith(CODE_BLOCK_PREFIX + DOCTEST_PREFIX, ignoreCase = true)
}

fun isDoctest(context: PsiElement): Boolean {
    return context.elementType.toString() == "KDOC_CODE_BLOCK_TEXT"
}
