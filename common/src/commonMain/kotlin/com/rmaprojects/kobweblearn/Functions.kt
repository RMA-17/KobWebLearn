package com.rmaprojects.kobweblearn

fun ApiResponse.parseAsString(): String {
    return when (this) {
        is ApiResponse.Error -> this.errorMessage
        is ApiResponse.Idle -> ""
        is ApiResponse.Success -> {
            this.data.joinToString("\n") {
                "Name: ${it.name} - Age: ${it.age}"
            }
        }
    }
}