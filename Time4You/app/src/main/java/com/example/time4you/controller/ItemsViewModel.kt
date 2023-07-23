package com.example.time4you.controller

import android.view.View

data class ItemsViewModel(val image: Int,
                          val text: String,
                          val button1ClickListener: View.OnClickListener?,
                          val button2ClickListener: View.OnClickListener?)

