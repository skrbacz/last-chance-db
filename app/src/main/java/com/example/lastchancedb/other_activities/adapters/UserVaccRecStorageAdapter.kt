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

/**
 * RecyclerView adapter for displaying vaccination records in a list.
 *
 * @param context The context of the calling activity or fragment.
 * @param vaccRecModelAL The list of vaccination record models to be displayed.
 * @param onDeleteClickListener The listener for the delete button click event.
 */
class UserVaccRecStorageAdapter(
    private val context: Context,
    private val vaccRecModelAL: ArrayList<VaccRecModel>,
    private val onDeleteClickListener: OnDeleteClickListener,
) : RecyclerView.Adapter<UserVaccRecStorageAdapter.ViewHolder>() {

    /**
     * Interface definition for a callback to be invoked when the delete button is clicked.
     */
    interface OnDeleteClickListener {
        /**
         * Called when the delete button is clicked.
         *
         * @param position The position of the item that was clicked.
         */
        fun onDeleteClick(position: Int)
    }


    /**
     * Gets the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return vaccRecModelAL.size
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccModel = vaccRecModelAL[position]
        holder.vaccName?.text = vaccModel.vaccName?.uppercase()
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

    /**
     * ViewHolder for the RecyclerView.
     *
     * @param itemView The inflated item view for the ViewHolder.
     */
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

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater= LayoutInflater.from(context)
        val view= inflater.inflate(R.layout.vacc_rec_history_item, parent, false)
        return ViewHolder(view)
    }
}
