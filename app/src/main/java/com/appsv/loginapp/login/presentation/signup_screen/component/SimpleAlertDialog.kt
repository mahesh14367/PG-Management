package com.appsv.loginapp.login.presentation.signup_screen.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleAlertDialog(
    title: String,
    text: String,
    confirmText:String="",
    dismissText:String="Ok",
    onConfirm:()->Unit,
    onDismiss:()->Unit,
    onDismissRequest:()->Unit
) {

    AlertDialog(
        onDismissRequest={onDismissRequest()},
        dismissButton = {
            if(dismissText =="No"){
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(text=dismissText)
                }
            }else{
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(text=dismissText)
                }
            }
        },
        confirmButton = {
            if(confirmText == "Yes"){
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text(text=confirmText)
                }
            }else{
            }

        },
        text = {
            Text(text=text)
        },
        title = {
            Text(text=title)
        }
    )

}