package com.example.randomuserexplorer

import com.example.randomuserexplorer.data.model.User
import com.example.randomuserexplorer.data.model.nestedmodels.Coordinates
import com.example.randomuserexplorer.data.model.nestedmodels.Dob
import com.example.randomuserexplorer.data.model.nestedmodels.Id
import com.example.randomuserexplorer.data.model.nestedmodels.Location
import com.example.randomuserexplorer.data.model.nestedmodels.Login
import com.example.randomuserexplorer.data.model.nestedmodels.Name
import com.example.randomuserexplorer.data.model.nestedmodels.Picture
import com.example.randomuserexplorer.data.model.nestedmodels.Registered
import com.example.randomuserexplorer.data.model.nestedmodels.Street
import com.example.randomuserexplorer.data.model.nestedmodels.Timezone
import com.example.randomuserexplorer.data.repository.UserRepository
import com.example.randomuserexplorer.viewmodel.UserViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {

    // Allows LiveData to be tested synchronously
//    @get:Rule
   // val instantExecutorRule = InstantTaskExecutorRule()

    // Enables testing of coroutines
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: UserRepository

    private lateinit var viewModel: UserViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadUsers should update userList when repository returns data`() = runTest {
        // Mock data
        val mockUsers = listOf(
            User(
                gender = "male",
                name = Name("Mr.",first = "John", last = "Doe"),
                location = Location(street = Street(0,""), city = "New York", state = "", country = "USA",postcode="", coordinates = Coordinates("",""), timezone = Timezone("","")),
                email = "john.doe@example.com",
                login = Login("",username = "johndoe123","","","","",""),
                dob = Dob("",age = 30),
                registered = Registered("",age = 5),
                phone = "123-456-7890",
                cell = "987-654-3210",
                id = Id(name = "SSN", value = "123-45-6789"),
                picture = Picture(large = "https://randomuser.me/api/portraits/men/1.jpg","",""),
                nat = "US"
            )
        )

        // Mock repository response
        whenever(repository.fetchUsers(1)).thenReturn(flowOf(mockUsers))

        // Call function
        viewModel.loadUsers(1)
        advanceUntilIdle() // Wait for coroutines to complete

        // Assert result
        Assert.assertEquals(mockUsers, viewModel.userList.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadUsers should handle repository error`() = runTest {
        // Mock the repository to return a flow that throws an exception
        whenever(repository.fetchUsers(any()))
            .thenReturn(flow { throw Exception("Network error") })  // Correct way to throw an error

        // Call the function
        viewModel.loadUsers(1)
        advanceUntilIdle()

        // Assert that userList remains empty
        Assert.assertEquals(emptyList<User>(), viewModel.userList.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadUsers should handle empty response`() = runTest {
        // Given: Mock repository returns an empty list
        whenever(repository.fetchUsers(any())).thenReturn(flowOf(emptyList()))

        // When: Calling loadUsers()
        viewModel.loadUsers(1)
        advanceUntilIdle()

        // Then: userList should be empty
        Assert.assertEquals(emptyList<User>(), viewModel.userList.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadUsers should set loading state correctly`() = runTest {
        val testUser = User(
            gender = "male",
            name = Name("Mr.", first = "John", last = "Doe"),
            location = Location(
                street = Street(0, ""), city = "New York", state = "",
                country = "USA", postcode = "", coordinates = Coordinates("", ""), timezone = Timezone("", "")
            ),
            email = "john.doe@example.com",
            login = Login("", username = "johndoe123", "", "", "", "", ""),
            dob = Dob("", age = 30),
            registered = Registered("", age = 5),
            phone = "123-456-7890",
            cell = "987-654-3210",
            id = Id(name = "SSN", value = "123-45-6789"),
            picture = Picture(large = "https://randomuser.me/api/portraits/men/1.jpg", "", ""),
            nat = "US"
        )

        // Use a delay to simulate network call
        val userFlow = flow {
            emit(emptyList<User>())  // Simulate initial empty state
            delay(100)               // Simulate delay in network call
            emit(listOf(testUser))   // Emit actual data
        }

        whenever(repository.fetchUsers(any())).thenReturn(userFlow)

        val isLoadingState = mutableListOf<Boolean>()

        // Collect `isLoading` states
        val job = launch { viewModel.isLoading.toList(isLoadingState) }

        // Call function
        viewModel.loadUsers(1)

        // Ensure all coroutines finish execution
        advanceTimeBy(200)  // Simulates passage of time
        advanceUntilIdle()  // Ensures coroutines complete

        // Stop collecting
        job.cancel()

        // Debug output
        println("isLoading states: $isLoadingState")

        // 🔥 Ensure correct transitions
        assertEquals(listOf(false, true, false), isLoadingState)
    }

}