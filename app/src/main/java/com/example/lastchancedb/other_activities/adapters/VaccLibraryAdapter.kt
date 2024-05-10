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

/**
 * RecyclerView adapter for displaying vaccination library items in a list.
 *
 * @param context The context of the calling activity or fragment.
 * @param vaccModelArrayList The list of vaccination models to be displayed.
 */
class VaccLibraryAdapter(
    private val context: Context,
    private val vaccModelArrayList: ArrayList<VaccModel>
) : RecyclerView.Adapter<VaccLibraryAdapter.ViewHolder>() {

    /**
     * Gets the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return vaccModelArrayList.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccModel = vaccModelArrayList[position]
        holder.vaccName?.text = vaccModel.name?.uppercase()
        holder.vaccManufacturer?.text = vaccModel.manufacturer
        holder.vaccInterval?.text = vaccModel.daysUntilNextDose.toString()
        holder.vaccImage?.setImageResource(R.drawable.ic_syringe_filled)
    }

    /**
     * ViewHolder for the RecyclerView.
     *
     * @param itemView The inflated item view for the ViewHolder.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vaccName: TextView? = itemView.findViewById(R.id.vaccNameTVLib)
        var vaccManufacturer: TextView? = itemView.findViewById(R.id.vaccDescriptionTVLib)
        var vaccInterval: TextView? = itemView.findViewById(R.id.vaccIntervalTVLib)
        var vaccImage: ImageView?= itemView.findViewById(R.id.vaccImageLib)
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
        val view= inflater.inflate(R.layout.vacc_library_item, parent, false)
        return ViewHolder(view)
    }
}
