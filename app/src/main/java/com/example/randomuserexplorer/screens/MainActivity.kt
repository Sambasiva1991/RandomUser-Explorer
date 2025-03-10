package com.example.randomuserexplorer.screens

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.randomuserexplorer.data.model.User
import com.example.randomuserexplorer.ui.theme.navigation.NavGraph
import com.example.randomuserexplorer.utiles.DummyData
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
    val errorMessage by viewModel.errorMessage.collectAsState()

    val userList by viewModel.userList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var userCount by remember { mutableStateOf("") }



    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {

        TopViewShow(userCount = userCount, onUserCountChange = { userCount = it })

        Spacer(modifier = Modifier.height(5.dp))

        FetchButton(userCount = userCount, onFetchClick = { count ->
                viewModel.loadUsers(count)
        })


        if (!errorMessage.isNullOrEmpty()) {
            Text("Error: $errorMessage", color = Color.Red)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(userList) { user ->
                UserCard(user,onClick = {
                    val userJson = Uri.encode(Gson().toJson(user))
                    navController.navigate("detail/${userJson}") })
            }

            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            // Trigger fetching more users when reaching the bottom
            item {
                LaunchedEffect(userList) {
                    if (!isLoading) {
                        viewModel.fetchNextPage()
                    }
                }
            }
        }


    }
}
@Composable
fun UserCard(user: User,onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .clickable { onClick() }
        .height(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp))

         {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(user.picture.thumbnail),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)

            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier
                .padding(2.dp)
                .weight(1f)
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Top) {
                Spacer(modifier = Modifier.height(2.dp))
                Text("${user.name.title}${user.name.first} ${user.name.last}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("${user.location.street.number}, ${user.location.street.name}, ${user.location.city}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text("${user.location.state}, ${user.location.country} - ${user.location.postcode}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp)
                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "View details>>",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    textAlign = TextAlign.Right,
                     )
            }
        }
    }
}

@Composable
fun TopViewShow(userCount: String, onUserCountChange: (String) -> Unit){

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Random Users",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(40.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            fontSize = 20.sp,
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "How many users do you need? Enter a number",
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            fontSize = 16.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(5.dp))

        TextField(value = userCount,
            onValueChange = {newText ->
                if (newText.all { it.isDigit() }) { // Allow only numbers
                    onUserCountChange(newText)
                }
            },
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(fontSize = 14.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(10.dp))




    }

}

@Composable
fun FetchButton(userCount: String, onFetchClick: (Int) -> Unit) {
    Button(
        onClick = {
            val count = userCount.toIntOrNull() ?: 0
            if (count > 0) {
                onFetchClick(count) // Pass user input to ViewModel
            }
        },
        enabled = userCount.isNotEmpty()
    ) {
        Text("Fetch Users")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {

    UserCard(
        user = DummyData.dummyUser,onClick = {}
    )


}

