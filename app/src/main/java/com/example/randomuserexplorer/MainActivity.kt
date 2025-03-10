package com.example.randomuserexplorer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
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
import com.example.randomuserexplorer.ui.theme.navigation.NavGraph
import com.example.randomuserexplorer.viewmodel.UserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            NavGraph(navController)
        }
    }
}

@Composable
fun UserListScreen(navController: NavController,viewModel: UserViewModel = hiltViewModel()) {
    val users by viewModel.userList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    Column {
        Button(onClick = { viewModel.loadUsers(5) }) {
            Text("Fetch Users")
        }

        if (isLoading) {
            CircularProgressIndicator() // Show Loading Indicator
        }

        if (!errorMessage.isNullOrEmpty()) {
            Text("Error: $errorMessage", color = Color.Red)
        }

        LazyColumn {
            items(users) { user ->

                UserCard(user,onClick = {
                    val userJson = Uri.encode(Gson().toJson(user))
                    navController.navigate("detail/${userJson}") })
            }
        }
    }
}
@Composable
fun UserCard(user: User,onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp).clickable { onClick() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(user.picture.thumbnail),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column {
                Text("${user.name.first} ${user.name.last}")
                Text(user.email)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {
    UserCard(
        user = User(
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
        ),onClick = {}
    )
}

@Composable
fun UserDetailScreen(userJson: String?) {
    val user = remember {
        userJson?.let {
            Gson().fromJson(it, User::class.java)
        }
    }

    user?.let {
        Column {
            Image(painter = rememberAsyncImagePainter(user.picture.thumbnail), contentDescription = null)
            Text("${user.name.first} ${user.name.last}")
            Text(user.email)
        }
    }
}
