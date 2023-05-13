package io.github.slava0135.doktestidea

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase

class DoctestCodeInsightTest : LightJavaCodeInsightFixtureTestCase() {
    override fun getTestDataPath() = "src/test/testData"

    fun testAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.kt")
        myFixture.checkHighlighting(true, false, false, false)
    }
}
