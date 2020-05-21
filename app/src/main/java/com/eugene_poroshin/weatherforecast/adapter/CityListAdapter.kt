package com.eugene_poroshin.weatherforecast.adapter

import android.content.Context
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

class CityListAdapter internal constructor(
    context: Context,
    communication: FragmentCommunicator
) : RecyclerView.Adapter<CityListAdapter.CityViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cities = emptyList<CityEntity>()
    private val communicator: FragmentCommunicator = communication

    inner class CityViewHolder(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = inflater.inflate(R.layout.city_list_item, parent, false)
        return CityViewHolder(
            view,
            communicator
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.imageView.setImageResource(R.drawable.ic_location_on_red_24dp)
        holder.textView.text = cities[position].name
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun setCities(cityList: List<CityEntity>) {
        cities = cityList
        notifyDataSetChanged()
    }
}