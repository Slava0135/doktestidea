package io.github.slava0135.doktestidea

import com.intellij.spellchecker.dictionary.Dictionary
import com.intellij.spellchecker.dictionary.RuntimeDictionaryProvider

class DoctestDictionaryProvider : RuntimeDictionaryProvider {
    override fun getDictionaries() = arrayOf(DoctestDictionary())
}

class DoctestDictionary : Dictionary {
    private val dict = DOCTEST_OPTIONS.keys + setOf(DOCTEST_PREFIX)

    override fun getName() = "Doktest dictionary"
    override fun contains(word: String) = word.lowercase() in dict
    override fun getWords() = dict.toMutableSet()
}
