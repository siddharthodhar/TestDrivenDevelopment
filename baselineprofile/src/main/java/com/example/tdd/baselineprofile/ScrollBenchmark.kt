package com.example.tdd.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ScrollBenchmark {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollNoCompilation() {
        benchmark(CompilationMode.None())
    }

    @Test
    fun scrollBaselineProfile() {
        benchmark(CompilationMode.Partial())
    }

    @Test
    fun scrollFullCompilation() {
        benchmark(CompilationMode.Full())
    }

    private fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "com.example.tdd",
            metrics = listOf(FrameTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.WARM,
            compilationMode = compilationMode
        ) {
            // Launch and wait for content
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.scrollable(true)), 10_000)

            // Scroll the list
            val list = device.findObject(By.scrollable(true))
            list?.let {
                it.setGestureMargin(device.displayWidth / 5)
                it.fling(Direction.DOWN)
                device.waitForIdle()
                it.fling(Direction.DOWN)
                device.waitForIdle()
                it.fling(Direction.UP)
                device.waitForIdle()
            }
        }
    }
}

