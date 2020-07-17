package com.eugene_poroshin.weatherforecast.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.myhttp.MyHttpClient
import com.eugene_poroshin.myhttp.Request
import com.eugene_poroshin.myhttp.Response
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
import java.net.HttpURLConnection
import java.net.URL
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

        override fun onItemClickToDelete(cityEntity: CityEntity) {
            viewModel.delete(cityEntity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

////////////////////////////////////////////////////////////////////
        App.appComponent.fragmentSubComponentBuilder().with(this).build().inject(this)
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
        viewModel.allCitiesLiveData.observe(viewLifecycleOwner, Observer { cities ->
            cities?.let { adapter.setCities(it) }
        })

        val touchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                private val background =
                    ColorDrawable(resources.getColor(R.color.deleteBackground))

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    Toast.makeText(requireActivity(), "on Move", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    adapter.showMenu(viewHolder.adapterPosition)
                    Toast.makeText(requireActivity(), "onSwiped", Toast.LENGTH_SHORT).show()
                }

                override fun onChildDraw(
                    canvas: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    val itemView = viewHolder.itemView
                    if (dX > 0) {
                        background.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.left + dX.toInt(),
                            itemView.bottom
                        )
                    } else if (dX < 0) {
                        background.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    }  else {
                        background.setBounds(0, 0, 0, 0)
                    }
                    background.draw(canvas)
                }
            }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.setOnScrollChangeListener { _, _, _, _, _ -> adapter.closeMenu() }

    }

    override fun onFinishEditDialog(inputText: String?) {
        validateCityName(inputText?.capitalize())
    }

    private fun validateCityName(cityName: String?) {
        val apiKey = Constants.API_KEY
        val url = String.format(Constants.GET_CURRENT_WEATHER_BY_CITY_NAME_METRIC, cityName, apiKey)

        Log.d(MY_LOG, url)
        lifecycleScope.launch {
            val response = getResponse(url)
            val body = response?.body

            if (response != null) {
//                Log.d(MY_LOG, body)
                when (response.code) {
                    HttpURLConnection.HTTP_OK -> {
                        showToast("The City has been successfully added")
                        viewModel.insert(CityEntity(name = cityName))
                    }
                    else -> {
                        showToast("Something went wrong")
                    }
                }
            }
        }
    }

    private suspend fun getResponse(myURL: String?): Response? {
        return withContext(Dispatchers.IO) {
            val url = URL(myURL)
            val request = Request(url)
            val myHttpClient = MyHttpClient()
            val response = myHttpClient.newCall(request)
            response
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