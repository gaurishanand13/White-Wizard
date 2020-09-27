package com.example.whitewizard.recycler_view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.whitewizard.R
import com.example.whitewizard.Room.Models.imageModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dashboard_item.view.*


class DashboardRecyclerView(val context: Context,val list : ArrayList<imageModel>): RecyclerView.Adapter<ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.dashboard_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(Uri.parse(list[position].bitmap)).noPlaceholder().centerCrop().fit().into(holder.itemView.dashboardImageView);
        holder.itemView.share.setOnClickListener {
            share(position)
        }
    }

    fun share(position: Int){

        val uri = Uri.parse(list[position].bitmap)
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri)
        whatsappIntent.type = "image/jpeg"
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context,"Whatsapp have not been installed",Toast.LENGTH_SHORT).show()
        }
    }
}