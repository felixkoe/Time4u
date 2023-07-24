package com.example.time4you.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.time4you.R
import com.example.time4you.model.ItemsViewModel

class CustomAdapter(private val mList: List<ItemsViewModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(itemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = itemsViewModel.text

        holder.buyButton.setOnClickListener(itemsViewModel.button1ClickListener)
        holder.useButton.setOnClickListener(itemsViewModel.button2ClickListener)

        holder.textView2.text = itemsViewModel.text2

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.picture_name)
        val buyButton: Button = itemView.findViewById(R.id.buy_button)
        val useButton: Button = itemView.findViewById(R.id.use_button)
        val textView2: TextView = itemView.findViewById(R.id.req_lvl)
    }
}
