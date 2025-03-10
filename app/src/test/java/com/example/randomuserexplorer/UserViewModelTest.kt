package com.example.randomuserexplorer

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
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
import com.example.randomuserexplorer.utiles.DummyData
import com.example.randomuserexplorer.viewmodel.UserViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.ArgumentMatchers.anyInt
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
    fun `loadUsers should fetch correct number of users based on input size`() = runTest {
        // Mock paginated data (3 pages of 10 users each)
        val page1Users = List(10) { index -> createMockUser("User${index + 1}") }
        val page2Users = List(10) { index -> createMockUser("User${index + 11}") }
        val page3Users = List(10) { index -> createMockUser("User${index + 21}") }

        // Simulate repository returning different pages in sequence
        whenever(repository.fetchUsers(10))
            .thenReturn(flowOf(page1Users))  // First call -> Page 1
            .thenReturn(flowOf(page2Users))  // Second call -> Page 2
            .thenReturn(flowOf(page3Users))  // Third call -> Page 3

        // Track state updates
        val collectedUsers = mutableListOf<List<User>>()
        val job = launch { viewModel.userList.collect { collectedUsers.add(it) } }

        // Load 30 users (3 pages of 10)
        viewModel.loadUsers(30)
        advanceUntilIdle()  // Ensure coroutines finish

        // Manually trigger pagination
        viewModel.fetchNextPage() // Fetch Page 2
        advanceUntilIdle()
        viewModel.fetchNextPage() // Fetch Page 3
        advanceUntilIdle()

        // Validate total fetched users
        Assert.assertEquals(30, collectedUsers.last().size)
        Assert.assertEquals("User1", collectedUsers.last()[0].name.first)
        Assert.assertEquals("User30", collectedUsers.last()[29].name.first)

        job.cancel()
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
    fun `loadUsers should set loading state correctly`() = runTest {
        val testUser = DummyData.dummyUser

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

    private fun createMockUser(name: String): User {
        return User(
            gender = "male",
            name = Name("Mr.", first = name, last = "Doe"),
            location = Location(
                street = Street(123, "Main St"), city = "Hyderabad", state = "Telangana",
                country = "India", postcode = "500072", coordinates = Coordinates("", ""),
                timezone = Timezone("", "")
            ),
            email = "$name@example.com",
            login = Login("", username = name.lowercase(), "", "", "", "", ""),
            dob = Dob("", age = 30),
            registered = Registered("", age = 5),
            phone = "123-456-7890",
            cell = "987-654-3210",
            id = Id(name = "SSN", value = "123-45-6789"),
            picture = Picture(large = "https://randomuser.me/api/portraits/men/1.jpg", "", ""),
            nat = "US"
        )
    }

}