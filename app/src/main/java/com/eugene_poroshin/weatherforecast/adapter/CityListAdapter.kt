package com.eugene_poroshin.weatherforecast.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.fragments.FragmentCommunicator
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity


class CityListAdapter(
    context: Context,
    communication: FragmentCommunicator
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cities = emptyList<CityEntity>()
    private val communicator: FragmentCommunicator = communication
    private val SHOW_MENU = 1
    private val HIDE_MENU = 2

    override fun getItemViewType(position: Int): Int {
        return if (cities[position].showMenu) {
            SHOW_MENU
        } else {
            HIDE_MENU
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if(viewType==SHOW_MENU){
            view = inflater.inflate(R.layout.city_list_remove_item, parent, false)
            MenuViewHolder(view, communicator)
        }else{
            view = inflater.inflate(R.layout.city_list_item, parent, false)
            CityViewHolder(view, communicator)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CityViewHolder) {
            holder.imageView.setImageResource(R.drawable.ic_location_on_red_24dp)
            holder.textView.text = cities[position].name
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun setCities(cityList: List<CityEntity>) {
        cities = cityList
        notifyDataSetChanged()
    }

    fun showMenu(position: Int) {
        for (i in cities.indices) {
            cities[i].showMenu = false
        }
        cities[position].showMenu = true
        notifyDataSetChanged()
    }

    fun closeMenu() {
        for (element in cities) {
            element.showMenu = false
        }
        notifyDataSetChanged()
    }

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

    inner class MenuViewHolder(
        itemView: View,
        mCommunicator: FragmentCommunicator
    ) : RecyclerView.ViewHolder(itemView) {

        private val mCommunication: FragmentCommunicator = mCommunicator

        init {
            itemView.setOnClickListener {
                mCommunication.onItemClickToDelete(cities[position])
            }
        }
    }
}