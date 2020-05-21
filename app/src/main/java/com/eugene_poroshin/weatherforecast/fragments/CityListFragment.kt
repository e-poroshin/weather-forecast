package com.eugene_poroshin.weatherforecast.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.adapter.CityListAdapter
import com.eugene_poroshin.weatherforecast.fragments.AddCityDialogFragment.EditNameDialogListener
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity
import com.eugene_poroshin.weatherforecast.viewmodel.CityViewModel
import com.eugene_poroshin.weatherforecast.weather.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import java.io.IOException
import java.util.*

class CityListFragment : Fragment(), EditNameDialogListener {
    private var toolbar: Toolbar? = null
    private var fabAddCity: FloatingActionButton? = null
    private var addCityDialogFragment: AddCityDialogFragment? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: CityListAdapter? = null
    private var cities: MutableList<CityEntity> = ArrayList()
    private var viewModel: CityViewModel? = null
    private var onOpenFragmentListener: OnOpenFragmentListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOpenFragmentListener) {
            onOpenFragmentListener = context
        }
    }

    private val communicator: FragmentCommunicator = object : FragmentCommunicator {
        override fun onItemClickListener(cityName: String?) {
            if (onOpenFragmentListener != null) {
                onOpenFragmentListener!!.onOpenForecastFragmentByCityName(cityName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_city_list, container, false)
        toolbar = view.findViewById(R.id.my_toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        fabAddCity = view.findViewById(R.id.fabAddCity)
        recyclerView = view.findViewById(R.id.recycler_view_city_list)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        toolbar!!.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar!!.title = "Choose a City"
        toolbar!!.setNavigationOnClickListener {
            if (onOpenFragmentListener != null) {
                onOpenFragmentListener!!.onOpenForecastFragment()
            }
        }
        fabAddCity!!.setOnClickListener {
            addCityDialogFragment = AddCityDialogFragment()
            addCityDialogFragment!!.setTargetFragment(this@CityListFragment, 1)
            addCityDialogFragment!!.show(
                parentFragmentManager,
                addCityDialogFragment!!.javaClass.name
            )
        }
        adapter = CityListAdapter(cities, communicator)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.adapter = adapter
        viewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        viewModel!!.liveData.observe(
            viewLifecycleOwner,
            Observer { cityEntities ->
                cities = cityEntities
                adapter!!.setCities(cities)
            })
    }

    override fun onFinishEditDialog(inputText: String?) {
        validateCityName(inputText?.capitalize())
    }

    private fun validateCityName(cityName: String?) {
        val apiKey = Constants.API_KEY
        val url = String.format(
            Constants.GET_CURRENT_WEATHER_BY_CITY_NAME_METRIC,
            cityName,
            apiKey
        )
        Log.d(MY_LOG, url)
        val request = Request.Builder().url(url).build()
        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showToast("Connection failed, try again please")
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val str = response.body!!.string()
                when {
                    str == "{\"cod\":\"404\",\"message\":\"city not found\"}" -> {
                        showToast("This city does not exist, try again please")
                    }
                    str.startsWith("{\"cod\":") -> {
                        showToast("Adding failed. Please contact the app developer")
                    }
                    else -> {
                        showToast("The City has been successfully added")
                        viewModel!!.insert(CityEntity(cityName))
                    }
                }
            }
        })
    }

    private fun showToast(message: String) {
        requireActivity().runOnUiThread { Toast.makeText(context, message, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroy() {
        super.onDestroy()
        onOpenFragmentListener = null
    }

    companion object {
        private const val MY_LOG = "MY_LOG"
        fun newInstance(): CityListFragment {
            return CityListFragment()
        }
    }
}