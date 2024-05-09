package com.example.lastchancedb.other_activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lastchancedb.R
import com.example.lastchancedb.other_activities.models.VaccModel

class VaccLibraryAdapter(
    private val context: Context,
    private val vaccModelArrayList: ArrayList<VaccModel>
) : RecyclerView.Adapter<VaccLibraryAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return vaccModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccModel = vaccModelArrayList[position]
        holder.vaccName?.text = vaccModel.name
//        holder.vaccProducer?.text = vaccModel.producer
        holder.vaccDescription?.text = vaccModel.description
        holder.vaccInterval?.text = vaccModel.daysUntilNextDose.toString()
        holder.vaccImage?.setImageResource(R.drawable.ic_syringe_filled)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vaccName: TextView? = itemView.findViewById(R.id.vaccNameTVLib)
//        var vaccProducer: TextView? = itemView.findViewById(R.id.vaccProducerTVLib)
        var vaccDescription: TextView? = itemView.findViewById(R.id.vaccDescriptionTVLib)
        var vaccInterval: TextView? = itemView.findViewById(R.id.vaccIntervalTVLib)
        var vaccImage: ImageView?= itemView.findViewById(R.id.vaccImageLib)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater= LayoutInflater.from(context)
        val view= inflater.inflate(R.layout.vacc_library_item, parent, false)
        return ViewHolder(view)
    }
}
