package com.example.tdd.view.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tdd.FakeTDDRepository
import com.example.tdd.viewmodels.HomeScreenViewModel
import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.entity.Data
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeRepo = FakeTDDRepository()

    private fun createViewModel(): HomeScreenViewModel {
        return HomeScreenViewModel(fakeRepo)
    }

    @Test
    fun homeScreen_displaysTitle() {
        val viewModel = createViewModel()
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel, onItemClick = {})
        }
        composeTestRule.onNodeWithText("TDD App").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsEmptyState_whenNoData() {
        val viewModel = createViewModel()
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel, onItemClick = {})
        }
        composeTestRule.onNodeWithText("No items yet. Pull down to refresh.")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsListItems_whenDataIsPresent() {
        fakeRepo.dataFlow.value = listOf(
            Data(userId = 1, id = 1, title = "Title One", body = "Body One"),
            Data(userId = 1, id = 2, title = "Title Two", body = "Body Two")
        )
        val viewModel = createViewModel()
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel, onItemClick = {})
        }
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("Title One"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Title One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Body One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title Two").assertIsDisplayed()
        composeTestRule.onNodeWithText("Body Two").assertIsDisplayed()
    }

    @Test
    fun homeScreen_emptyStateDisappears_whenDataLoads() {
        val viewModel = createViewModel()
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel, onItemClick = {})
        }
        composeTestRule.onNodeWithText("No items yet. Pull down to refresh.")
            .assertIsDisplayed()

        fakeRepo.dataFlow.value = listOf(
            Data(userId = 1, id = 1, title = "New Title", body = "New Body")
        )
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("New Title"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("New Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("No items yet. Pull down to refresh.")
            .assertDoesNotExist()
    }

    @Test
    fun homeScreen_itemClick_triggersCallback() {
        fakeRepo.dataFlow.value = listOf(
            Data(userId = 1, id = 42, title = "Clickable Item", body = "Click me")
        )
        var clickedId = -1
        val viewModel = createViewModel()
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel, onItemClick = { id -> clickedId = id })
        }
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("Clickable Item"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Clickable Item").performClick()
        assertEquals(42, clickedId)
    }

    @Test
    fun homeScreen_showsSnackbar_onFetchError() {
        fakeRepo.fetchDataFlow.value =
            APIResult.Error(RuntimeException("Network failure"))
        val viewModel = createViewModel()
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel, onItemClick = {})
        }
        viewModel.fetchData()

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodes(hasText("Network failure"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Network failure").assertIsDisplayed()
    }
}

