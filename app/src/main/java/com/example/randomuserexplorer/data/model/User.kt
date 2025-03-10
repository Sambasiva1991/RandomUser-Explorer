package com.example.randomuserexplorer.data.model

import com.example.randomuserexplorer.data.model.nestedmodels.Dob
import com.example.randomuserexplorer.data.model.nestedmodels.Id
import com.example.randomuserexplorer.data.model.nestedmodels.Location
import com.example.randomuserexplorer.data.model.nestedmodels.Login
import com.example.randomuserexplorer.data.model.nestedmodels.Name
import com.example.randomuserexplorer.data.model.nestedmodels.Picture
import com.example.randomuserexplorer.data.model.nestedmodels.Registered

data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val registered: Registered,
    val phone: String,
    val cell: String,
    val id: Id,
    val picture: Picture,
    val nat: String
)

