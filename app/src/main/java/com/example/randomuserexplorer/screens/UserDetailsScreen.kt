package com.example.randomuserexplorer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.randomuserexplorer.R
import com.example.randomuserexplorer.data.model.User
import com.example.randomuserexplorer.utiles.DummyData
import com.google.gson.Gson

@Composable
fun UserInfoDetailsScreen(navController: NavController?, userJson: String?) {

    val user = remember {
        userJson?.let {
            Gson().fromJson(it, User::class.java)
        }
    }
    user?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                IconButton(onClick = {
                    navController?.popBackStack()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Drawable Image",
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    text = "${user.name.first} ${user.name.last} Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = rememberAsyncImagePainter(model = user.picture.medium),
                contentDescription = "Url Image",
                modifier = Modifier
                    .background(
                        Color.LightGray
                    )
                    .size(150.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(5.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "First Name :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.name.first,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "Last Name :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.name.last,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "Gender        :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.gender,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "DOB             :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.dob.date,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "Phone         :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.phone,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "State           :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.location.state,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "Country      :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.location.country,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "Address      :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "${user.location.street.name}, ${user.location.city}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
                Text(
                    text = "Pin code      :  ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = user.location.postcode,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }


        }

    }

}

@Preview(showBackground = true)
@Composable
fun UserDetailsPreview(){

    UserInfoDetailsScreen(null,DummyData.dummyUser.toString())

}

