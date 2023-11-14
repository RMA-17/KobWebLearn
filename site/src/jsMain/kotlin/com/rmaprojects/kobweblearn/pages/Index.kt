package com.rmaprojects.kobweblearn.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.rmaprojects.kobweblearn.ApiResponse
import com.rmaprojects.kobweblearn.parseAsString
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLInputElement

//Every composable contains @Page represents the actual page of the website

/**
 * Some of composable components not receiving Modifier to modify the appearance.
 * Instead they receive attrs attribute which means to be the same as CSS.
 * But we can actually fill the attrs params with Modifier.toAttr()
 */
@Page
@Composable
fun HomePage() {

    val scope = rememberCoroutineScope()

    var apiResponseText by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize().margin(top = 200.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontSize(18.px)
                .fontFamily("Roboto")
                .fontWeight(FontWeight.Bold),
            text = "Specify amount of people you want to get"
        )
        Input(
            type = InputType.Number,
            attrs = Modifier
                .id("countInput")
                .margin(bottom = 16.px)
                .fontSize(14.px)
                .fontFamily("Roboto")
                .toAttrs {
                    attr("placeholder", "Enter the number")
                }
        )
        Button(
            attrs = Modifier
                .margin(bottom = 16.px)
                .fontSize(16.px)
                .fontFamily("Roboto")
                .onClick {
                    //Place onClick for this Component in Modifier
                    scope.launch {
                        val apiResponse = fetchData()
                        apiResponseText = apiResponse.parseAsString()
                    }
                }
                .toAttrs()
        ) {
            SpanText("Fetch People")
        }

        //Paragraph in HTML:
        P(
            attrs = Modifier
                .margin(bottom = 16.px)
                .fontSize(14.px)
                .fontFamily("Roboto")
                .toAttrs()
        ) {
            //Putting "P" is not enough, you must put "Text" inside the content param
            Text(apiResponseText)
        }
    }
}

private suspend fun fetchData(): ApiResponse {
    val inputText = (document.getElementById("countInput") as HTMLInputElement).value
    val number = if (inputText.isEmpty()) 0 else inputText.toInt()
    //To get/fetch API use .api. Just like Axios
    //There are 2 get request, the one with "try" and just "get"
    val result = window.api.tryGet(apiPath = "getpeople?count=$number")?.decodeToString()

    return Json.decodeFromString(result.toString())
}