package com.example.medictionary

import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.errorprone.annotations.Var

class ListAdapter(var mCtx:Context,var resources:Int,var items:List<Model>):ArrayAdapter<Model>(mCtx,resources,items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       val layoutInflater:LayoutInflater= LayoutInflater.from(mCtx)
        val view:View=layoutInflater.inflate(resources,null)

        val imageView:ImageView=view.findViewById(R.id.imageBox)
        val titleTextView:TextView=view.findViewById(R.id.nameTxt)
        val descriptionTextView:TextView=view.findViewById(R.id.desTxt)

        var mItem:Model=items[position]
        imageView.setImageDrawable(mCtx.resources.getDrawable(mItem.img))
        titleTextView.text=mItem.name
        descriptionTextView.text=mItem.des

        return view
    }
}