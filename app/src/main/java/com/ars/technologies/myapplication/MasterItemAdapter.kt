package com.ars.technologies.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MasterItemAdapter(private val mList: List<Master>) : RecyclerView.Adapter<MasterItemAdapter.MasterItemViewHolder>() {

//
//    var data = listOf<Master>()
//        set(value){
//            field=value
//            notifyDataSetChanged()
//        }

//    override fun onBindViewHolder(holder:MasterItemViewHolder,position:Int){
//        val item = data[position]
//        holder.bind(item)
//    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: MasterItemViewHolder, position: Int) {

        val masterItem = mList[position]

//        holder.sno.text = (position+1).toString()
        holder.code.text=(masterItem.code)
        holder.description.text=(masterItem.description)

//        holder.rate.text=(masterItem.rate.toString())
        if(masterItem.quantity != null) {
            holder.quantity.text = (masterItem.quantity.toString())
            holder.unit.text=(masterItem.unit)
        }


    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterItemViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.master_view_design, parent, false)

        return MasterItemViewHolder(view)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterItemViewHolder = MasterItemViewHolder.inflateFrom(parent)

    override fun getItemCount() = mList.size

    class MasterItemViewHolder(val rootView : View):RecyclerView.ViewHolder(rootView){

//        val sno: TextView = itemView.findViewById(R.id.sno)
        val code: TextView = itemView.findViewById(R.id.code)
        val description: TextView = itemView.findViewById(R.id.description)
        val unit: TextView = itemView.findViewById(R.id.unit)
//        val rate: TextView = itemView.findViewById(R.id.rate)
        val quantity: TextView = itemView.findViewById(R.id.qty)

//        companion object{
//            fun inflateFrom(parent:ViewGroup):MasterItemViewHolder{
//                val layoutInflater  = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.master_item,parent,false) as TextView
//                return MasterItemViewHolder(view)
//            }
//        }

//        fun bind(item:Master){
//            rootView.text = item.code
//
//        }

    }

}