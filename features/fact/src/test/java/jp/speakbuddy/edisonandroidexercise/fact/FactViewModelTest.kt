package jp.speakbuddy.edisonandroidexercise.fact

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import jp.speakbuddy.edisonandroidexercise.domain.model.FactCatDomainModel
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCase
import jp.speakbuddy.edisonandroidexercise.domain.util.DomainResult
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatDetail
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatUiState
import jp.speakbuddy.edisonandroidexercise.fact.ui.FactViewModel
import jp.speakbuddy.edisonandroidexercise.testing.rule.UnconfinedTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FactViewModelTest {

    @get:Rule
    val coroutineScopeRule = UnconfinedTestRule()

    private lateinit var viewModel: FactViewModel

    @RelaxedMockK
    private lateinit var useCase: FactCatUseCase

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
        viewModel = FactViewModel(useCase)
    }

    private fun mockCatDomainModel() = FactCatDomainModel(
        fact = "fact 1",
        length = 10,
        containsCats = false
    )

    @Test
    fun showInitialLoading_whenFirstOpen() {
        val detail = viewModel.uiState.value.detail
        val buttonLoading = viewModel.uiState.value.isLoading

        assert(detail is FactCatDetail.InitialLoading)
        assert(!buttonLoading)
    }

    @Test
    fun showSuccessData_whenFirstOpen() = runTest {
        // Given
        val mockFact = mockCatDomainModel()
        coEvery { useCase.getFact(true) } returns flowOf(DomainResult.Success(mockFact))

        // When
        val states = mutableListOf<FactCatUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(states)
        }

        // Then
        assertEquals(2, states.size) // Initial, Loading, Success
        with(states) {
            assert(get(0).detail is FactCatDetail.InitialLoading)
            assert(get(1).detail is FactCatDetail.Success)
        }

        job.cancel()
    }

    @Test
    fun showSuccessData_whenClickUpdate() = runTest() {
        // Given
        val mockFact = mockCatDomainModel()
        coEvery {
            useCase.getFact(true)
        } returns flowOf(DomainResult.Success(mockFact))

        coEvery {
            useCase.getFact(false)
        } returns flowOf(DomainResult.Success(mockFact.copy(fact = "fact 2")))

        // When
        val states = mutableListOf<FactCatUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {
                states.add(it)
            }
        }

        viewModel.updateFact()

        // Then
        assertEquals(4, states.size) // Initial, Loading, Success

        //initial open
        val firstState = states.first()
        assertTrue(firstState.detail is FactCatDetail.InitialLoading)
        assertTrue(!firstState.isLoading)

        //get initial data
        val secondState = states[1]
        assertTrue(secondState.detail is FactCatDetail.Success)
        assertTrue((secondState.detail as FactCatDetail.Success).uiModel.fact == "fact 1")
        assertTrue(!secondState.isLoading)
        assertTrue(secondState.toasterState == null)

        //its loading the button after user click update
        val thridState = states[2]
        assertTrue(thridState.detail is FactCatDetail.Success)
        assertTrue((thridState.detail as FactCatDetail.Success).uiModel.fact == "fact 1")
        assertTrue(thridState.isLoading)
        assertTrue(secondState.toasterState == null)

        //its latest success data after user click update
        val fourthState = states[3]
        assertTrue(fourthState.detail is FactCatDetail.Success)
        assertTrue((fourthState.detail as FactCatDetail.Success).uiModel.fact == "fact 2")
        assertTrue(!fourthState.isLoading)
        assertTrue(secondState.toasterState == null)
    }

    @Test
    fun showFullError_whenUseCaseThrows() = runTest {
        coEvery {
            useCase.getFact(true)
        } throws Throwable("Error")

        // When
        val states = mutableListOf<FactCatUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {
                states.add(it)
            }
        }

        //Initial open
        val firstState = states.first()
        assertTrue(firstState.detail is FactCatDetail.InitialLoading)
        assertTrue(!firstState.isLoading)

        val lastState = states.last()
        assertTrue(lastState.detail is FactCatDetail.Fail)
        assertTrue(!lastState.isLoading)
        assertTrue(lastState.toasterState == null)
    }

    @Test
    fun showError_whenGetFactData() = runTest {
        coEvery {
            useCase.getFact(true)
        } returns flowOf(DomainResult.Error("Error"))

        // When
        val states = mutableListOf<FactCatUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {
                states.add(it)
            }
        }

        //Initial open
        val firstState = states.first()
        assertTrue(firstState.detail is FactCatDetail.InitialLoading)
        assertTrue(!firstState.isLoading)

        val lastState = states.last()
        assertTrue((lastState.detail as FactCatDetail.Fail).errorMessage == "Error")
        assertTrue(!lastState.isLoading)
        assertTrue(lastState.toasterState == null)
    }

    @Test
    fun showToasterError_afterClickUpdate() = runTest() {
        // Given
        val mockFact = mockCatDomainModel()
        coEvery {
            useCase.getFact(true)
        } returns flowOf(DomainResult.Success(mockFact))

        coEvery {
            useCase.getFact(false)
        } returns flowOf(DomainResult.Error("Error"))

        // When
        val states = mutableListOf<FactCatUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {
                states.add(it)
            }
        }

        viewModel.updateFact()

        // Then
        assertEquals(4, states.size) // Initial, Loading, Success

        //initial open
        val firstState = states.first()
        assertTrue(firstState.detail is FactCatDetail.InitialLoading)
        assertTrue(!firstState.isLoading)
        assertTrue(firstState.toasterState == null)

        //get initial data
        val secondState = states[1]
        assertTrue(secondState.detail is FactCatDetail.Success)
        assertTrue((secondState.detail as FactCatDetail.Success).uiModel.fact == "fact 1")
        assertTrue(!secondState.isLoading)
        assertTrue(secondState.toasterState == null)

        //its loading the button after user click update
        val thridState = states[2]
        assertTrue(thridState.detail is FactCatDetail.Success)
        assertTrue((thridState.detail as FactCatDetail.Success).uiModel.fact == "fact 1")
        assertTrue(thridState.isLoading)
        assertTrue(thridState.toasterState == null)

        //its latest error data after user click update
        val fourthState = states[3]
        //ensure still success
        assertTrue(fourthState.detail is FactCatDetail.Success)
        assertTrue(!fourthState.isLoading)
        //show toast
        assertTrue(fourthState.toasterState != null)
    }
}