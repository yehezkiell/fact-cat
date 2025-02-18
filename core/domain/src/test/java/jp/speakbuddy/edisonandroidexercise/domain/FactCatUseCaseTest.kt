package jp.speakbuddy.edisonandroidexercise.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import jp.speakbuddy.edisonandroidexercise.data.model.FactCatDataModel
import jp.speakbuddy.edisonandroidexercise.data.repository.DataResult
import jp.speakbuddy.edisonandroidexercise.data.repository.FactCatRepository
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCase
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCaseImpl
import jp.speakbuddy.edisonandroidexercise.domain.util.DomainResult
import jp.speakbuddy.edisonandroidexercise.testing.rule.UnconfinedTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FactCatUseCaseTest {

    @get:Rule
    val coroutineScopeRule = UnconfinedTestRule()

    private lateinit var useCase: FactCatUseCase

    @RelaxedMockK
    private lateinit var repository: FactCatRepository

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
        useCase = FactCatUseCaseImpl(repository)
    }

    private val mockCatModel
        get() = FactCatDataModel(
            fact = "fact 1",
            length = 10
        )

    @Test
    fun getFactUseCase_shouldReturnSuccess() = runTest {
        coEvery {
            repository.getFact(true)
        } returns flowOf(DataResult.Success(mockCatModel))

        val result = useCase.getFact(true).first()

        coEvery {
            repository.getFact(true)
        }

        Assert.assertTrue((result as DomainResult.Success).data.fact == "fact 1")
        Assert.assertTrue(result.data.length == 10)
    }

    @Test
    fun getFactUseCase_containsCatsString_shouldReturnSuccess() = runTest {
        coEvery {
            repository.getFact(true)
        } returns flowOf(DataResult.Success(mockCatModel.copy(fact = "cats", length = 10)))

        val result = useCase.getFact(true).first()

        coEvery {
            repository.getFact(true)
        }

        Assert.assertTrue((result as DomainResult.Success).data.fact == "cats")
        Assert.assertTrue(result.data.containsCats)
        Assert.assertTrue(result.data.length == 10)
    }

    @Test
    fun getFactUseCase_shouldReturnError() = runTest {
        coEvery {
            repository.getFact(true)
        } returns flowOf(DataResult.Error(Throwable("error")))

        val result = useCase.getFact(true).first()

        coEvery {
            repository.getFact(true)
        }

        Assert.assertTrue((result as DomainResult.Error).message == "error")
    }
}