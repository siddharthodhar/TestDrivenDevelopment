package com.example.tdd.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmark {

    @get:Rule
    val rule = MacrobenchmarkRule()

    /**
     * No compilation — simulates a fresh install with zero optimization.
     * This is the WORST case scenario for your users.
     */
    @Test
    fun startupNoCompilation() {
        benchmark(CompilationMode.None())
    }

    /**
     * Partial compilation — simulates a user who has your baseline profile.
     * This is what REAL users experience on first launch after install.
     */
    @Test
    fun startupBaselineProfile() {
        benchmark(CompilationMode.Partial())
    }

    /**
     * Full compilation — everything AOT compiled.
     * This is the BEST possible case (but unrealistic for real users).
     */
    @Test
    fun startupFullCompilation() {
        benchmark(CompilationMode.Full())
    }

    private fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "com.example.tdd",
            metrics = listOf(StartupTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.COLD,
            compilationMode = compilationMode
        ) {
            pressHome()
            startActivityAndWait()
        }
    }
}

