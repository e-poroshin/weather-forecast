package com.eugene_poroshin.weatherforecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.fragments.FragmentCommunicator
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity
import java.util.*

class CityListAdapter(
    cityList: MutableList<CityEntity>,
    communication: FragmentCommunicator
) : RecyclerView.Adapter<CityListAdapter.RecyclerViewHolder>() {

    private var cities: MutableList<CityEntity>?
    private val communicator: FragmentCommunicator

    init {
        cities = ArrayList(cityList)
        communicator = communication
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.city_list_item, parent, false)
        return RecyclerViewHolder(
            view,
            communicator
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int
    ) {
        holder.imageView.setImageResource(R.drawable.ic_location_on_red_24dp)
        holder.textView.text = cities!![position].name
    }

    override fun getItemCount(): Int {
        return if (cities != null) {
            cities!!.size
        } else 0
    }

    fun setCities(cityList: MutableList<CityEntity>) {
        cities = cityList
        notifyDataSetChanged()
    }

    inner class RecyclerViewHolder(
        itemView: View,
        mCommunicator: FragmentCommunicator
    ) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val textView: TextView = itemView.findViewById(R.id.itemTextView)
        private val mCommunication: FragmentCommunicator = mCommunicator

        init {
            itemView.setOnClickListener {
                val cityName = textView.text.toString()
                mCommunication.onItemClickListener(cityName)
            }
        }
    }

}