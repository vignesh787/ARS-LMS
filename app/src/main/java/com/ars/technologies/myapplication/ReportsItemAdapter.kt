package com.ars.technologies.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

class ReportsItemAdapter(private val mList: List<Transactions>) : RecyclerView.Adapter<ReportsItemAdapter.ReportsItemViewHolder>() {
    // binds the list items to a view
    override fun onBindViewHolder(holder: ReportsItemViewHolder, position: Int) {

        val reportsItem = mList[position]

//        holder.sno.text = (position+1).toString()
        holder.code.text=(reportsItem.code)
//        holder.description.text=(reportsItem.description)

//        holder.rate.text=(reportsItem.rate.toString())
        if(reportsItem.quantity != null) {
            holder.quantity.text = (reportsItem.quantity.toString())
            holder.unit.text=(reportsItem.unit)
        }


    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsItemViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.master_view_design, parent, false)

        return ReportsItemViewHolder(view)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterItemViewHolder = MasterItemViewHolder.inflateFrom(parent)

    override fun getItemCount() = mList.size

    class ReportsItemViewHolder(val rootView : View):RecyclerView.ViewHolder(rootView){

//        val sno: TextView = itemView.findViewById(R.id.sno)
        val code: TextView = itemView.findViewById(R.id.code)
        val description: TextView = itemView.findViewById(R.id.description)
        val unit: TextView = itemView.findViewById(R.id.unit)
        val rate: TextView = itemView.findViewById(R.id.rate)
        val quantity: TextView = itemView.findViewById(R.id.qty)

//
    }

}