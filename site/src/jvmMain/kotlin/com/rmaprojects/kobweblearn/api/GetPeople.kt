package com.rmaprojects.kobweblearn.api

import com.rmaprojects.kobweblearn.ApiResponse
import com.rmaprojects.kobweblearn.Person
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val people = listOf(
    Person("Mamang", 23),
    Person("Miming", 22),
    Person("Mumung", 21)
)

@Api
suspend fun getPeople(context: ApiContext) {
    try {
        //Getting request from parameter:
        val number = context.req.params.getValue("count").toInt()

        //Sending response as Json:
        context.res.setBodyText(
            Json.encodeToString<ApiResponse>(
                ApiResponse.Success(people.take(number))
            )
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            Json.encodeToString<ApiResponse>(
                ApiResponse.Error(e.message.toString())
            )
        )
    }
}