package com.example.tdd.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
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
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() {
        rule.collect(
            packageName = "com.example.tdd",
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait()

            device.wait(Until.hasObject(By.scrollable(true)), 10_000)

            val list = device.findObject(By.scrollable(true))
            list?.let {
                it.setGestureMargin(device.displayWidth / 5)
                it.fling(Direction.DOWN)
                device.waitForIdle()
                it.fling(Direction.UP)
                device.waitForIdle()
            }

            val firstItem = device.findObject(By.clickable(true))
            firstItem?.click()
            device.waitForIdle()

            device.wait(Until.hasObject(By.textContains("")), 5_000)
            device.pressBack()
            device.waitForIdle()
        }
    }
}

