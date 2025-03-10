package com.example.randomuserexplorer.utiles

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

object DummyData {

    val dummyUser = User(
        gender = "male",
        name = Name("Mr.", first = "John", last = "Doe"),
        location = Location(
            street = Street(123, "Vp Nagar"),
            city = "Hyderabad",
            state = "Telangana",
            country = "India",
            postcode = "500072",
            coordinates = Coordinates("", ""),
            timezone = Timezone("", "")
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
}