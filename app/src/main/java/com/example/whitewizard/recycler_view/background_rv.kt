package com.example.whitewizard.recycler_view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.whitewizard.R
import kotlinx.android.synthetic.main.background_item_view.view.*


class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)




class BackgroundAdapter(val context: Context,val imageView: ImageView): RecyclerView.Adapter<ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.background_item_view,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position==0){
            holder.itemView.backgroundImageViewItem.setImageResource(R.drawable.image_one)
        }
        else if(position==1){
            holder.itemView.backgroundImageViewItem.setImageResource(R.drawable.image_two)
        }
        else if(position==2){
            holder.itemView.backgroundImageViewItem.setImageResource(R.drawable.image_three)
        }
        else if(position==3){
            holder.itemView.backgroundImageViewItem.setImageResource(R.drawable.image_four)
        }
        else if(position==4){
            holder.itemView.backgroundImageViewItem.setImageResource(R.drawable.image_five)
        }
        else {
            holder.itemView.backgroundImageViewItem.setImageResource(R.drawable.image_six)
        }
        setUpOnClickListener(holder,position,imageView)
    }

    private fun setUpOnClickListener(holder: ViewHolder,position: Int,imageView: ImageView) {
        var selectedSrc : Int = R.drawable.image_one
        if(position==0){
            selectedSrc = R.drawable.image_one
        }
        else if(position==1){
            selectedSrc = R.drawable.image_two
        }
        else if(position==2){
            selectedSrc = R.drawable.image_three
        }
        else if(position==3){
            selectedSrc = R.drawable.image_four
        }
        else if(position==4){
            selectedSrc = R.drawable.image_five
        }
        else {
            selectedSrc = R.drawable.image_six
        }
        holder.itemView.setOnClickListener {
            //selectedSrc needs to be displayed on the required image view in the background
            imageView.setImageResource(selectedSrc)
        }
    }
}