package com.example.lastchancedb.other_activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lastchancedb.R
import com.example.lastchancedb.other_activities.models.VaccRecModel

class UserVaccRecStorageAdapter(
    private val context: Context,
    private val vaccRecModelAL: ArrayList<VaccRecModel>,
    private val onDeleteClickListener: OnDeleteClickListener,
) : RecyclerView.Adapter<UserVaccRecStorageAdapter.ViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    interface OnEditClickListener {
        fun onEditClick(position: Int)
    }


    override fun getItemCount(): Int {
        return vaccRecModelAL.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccModel = vaccRecModelAL[position]
        holder.vaccName?.text = vaccModel.vaccName
        holder.vaccDateAdministrated?.text = vaccModel.dateAdministrated.toString()

        // Check if nextDoseDueDate is equal to epoch time
        val nextDoseDueDate = vaccModel.nextDoseDueDate
        val doseDueText = if (nextDoseDueDate != null && nextDoseDueDate.time == 0L) {
            "not yet taken"
        } else {
            nextDoseDueDate?.toString() ?: ""
        }

        holder.vaccNextDoseDueDate?.text = doseDueText
        holder.vaccRecIMG?.setImageResource(R.drawable.ic_fab_add_record)
        holder.deleteIMG?.setImageResource(R.drawable.ic_trash_can)

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vaccName: TextView? = itemView.findViewById(R.id.vaccNameTVI)
        var vaccDateAdministrated: TextView? = itemView.findViewById(R.id.firstDoseDateTVH)
        var vaccNextDoseDueDate: TextView? = itemView.findViewById(R.id.nextDoseTVH)
        var vaccRecIMG: ImageView? = itemView.findViewById(R.id.vaccRecIMG)
        var deleteIMG: ImageView?= itemView.findViewById(R.id.deleteRecord)

        init {
            deleteIMG?.setOnClickListener {
                val position= adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDeleteClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater= LayoutInflater.from(context)
        val view= inflater.inflate(R.layout.vacc_rec_history_item, parent, false)
        return ViewHolder(view)
    }
}
