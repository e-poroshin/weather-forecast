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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.adapter.CityListAdapter
import com.eugene_poroshin.weatherforecast.di.App
import com.eugene_poroshin.weatherforecast.fragments.AddCityDialogFragment.EditNameDialogListener
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity
import com.eugene_poroshin.weatherforecast.viewmodel.CityViewModel
import com.eugene_poroshin.weatherforecast.weather.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class CityListFragment : Fragment(), EditNameDialogListener {

    @Inject
    lateinit var viewModel: CityViewModel

    private lateinit var toolbar: Toolbar
    private lateinit var fabAddCity: FloatingActionButton
    private lateinit var addCityDialogFragment: AddCityDialogFragment
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CityListAdapter

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

////////////////////////////////////////////////////////////////////
        App.appComponent.inject(this)
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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Choose a City"
        toolbar.setNavigationOnClickListener {
            onOpenFragmentListener!!.onOpenForecastFragment()
        }
        fabAddCity.setOnClickListener {
            addCityDialogFragment = AddCityDialogFragment()
            addCityDialogFragment.setTargetFragment(this@CityListFragment, 1)
            addCityDialogFragment.show(
                parentFragmentManager,
                addCityDialogFragment.javaClass.name
            )
        }
        adapter = CityListAdapter(requireContext(), communicator)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

////////////////////////////////////////////////////////////////////
//        viewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        viewModel.allCitiesLiveData.observe(viewLifecycleOwner, Observer { cities ->
            cities?.let { adapter.setCities(it) }
        })
    }

    override fun onFinishEditDialog(inputText: String?) {
        validateCityName(inputText?.capitalize())
    }

    private fun validateCityName(cityName: String?) {
        val apiKey = Constants.API_KEY
        val url = String.format(Constants.GET_CURRENT_WEATHER_BY_CITY_NAME_METRIC, cityName, apiKey)

        lifecycleScope.launch {
            val result = getResponse(url)
            Log.d(MY_LOG, result)
            when {
                result == "{\"cod\":\"404\",\"message\":\"city not found\"}" -> {
                    showToast("This city does not exist, try again please")
                }
                result.startsWith("{\"cod\":") -> {
                    showToast("Adding failed. Please contact the app developer")
                }
                else -> {
                    showToast("The City has been successfully added")
                    viewModel.insert(CityEntity(name = cityName))
                }
            }
        }
    }

    private suspend fun getResponse(myURL: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder().url(myURL).build()
            val okHttpClient = OkHttpClient()
            val str = okHttpClient.newCall(request).execute().body!!.string()
            str
        }
    }

    private fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
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