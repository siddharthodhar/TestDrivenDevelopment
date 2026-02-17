package com.example.tdd.view.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tdd.FakeTDDRepository
import com.example.tdd.viewmodels.DetailScreenViewModel
import com.example.tdd.viewmodels.domain.entity.Data
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeRepo = FakeTDDRepository()

    private fun createViewModel(id: Int = 1): DetailScreenViewModel {
        return DetailScreenViewModel(fakeRepo, id)
    }

    @Test
    fun detailScreen_displaysTitle() {
        val viewModel = createViewModel()
        composeTestRule.setContent {
            DetailScreen(viewModel = viewModel, onBackClick = {})
        }
        composeTestRule.onNodeWithText("Detail").assertIsDisplayed()
    }

    @Test
    fun detailScreen_showsBackButton() {
        val viewModel = createViewModel()
        composeTestRule.setContent {
            DetailScreen(viewModel = viewModel, onBackClick = {})
        }
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun detailScreen_showsLoadingIndicator_whenDataIsNull() {
        val viewModel = createViewModel()
        composeTestRule.setContent {
            DetailScreen(viewModel = viewModel, onBackClick = {})
        }
        // CircularProgressIndicator should be visible when data is null
        // We verify by checking that no title/body content is shown
        composeTestRule.onNodeWithText("Detail").assertIsDisplayed()
        // The data content should not exist yet
        composeTestRule.onNodeWithText("Test Title").assertDoesNotExist()
    }

    @Test
    fun detailScreen_showsContent_whenDataIsLoaded() {
        val testData = Data(
            userId = 1, id = 1,
            title = "Test Title", body = "Test Body Content"
        )
        fakeRepo.getOrCreateDataByIdFlow(1).value = testData

        val viewModel = createViewModel(id = 1)
        composeTestRule.setContent {
            DetailScreen(viewModel = viewModel, onBackClick = {})
        }
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("Test Title"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Body Content").assertIsDisplayed()
    }

    @Test
    fun detailScreen_loadingDisappears_whenDataArrives() {
        val viewModel = createViewModel(id = 2)
        composeTestRule.setContent {
            DetailScreen(viewModel = viewModel, onBackClick = {})
        }
        // Initially no content
        composeTestRule.onNodeWithText("Loaded Title").assertDoesNotExist()

        // Emit data
        fakeRepo.getOrCreateDataByIdFlow(2).value = Data(
            userId = 1, id = 2,
            title = "Loaded Title", body = "Loaded Body"
        )
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("Loaded Title"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Loaded Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Loaded Body").assertIsDisplayed()
    }

    @Test
    fun detailScreen_backClick_triggersCallback() {
        val viewModel = createViewModel()
        var backClicked = false
        composeTestRule.setContent {
            DetailScreen(viewModel = viewModel, onBackClick = { backClicked = true })
        }
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backClicked)
    }
}

