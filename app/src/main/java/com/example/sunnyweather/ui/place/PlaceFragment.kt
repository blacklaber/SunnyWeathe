package com.example.sunnyweather.ui.place

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R

class PlaceFragment: Fragment() {

    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView)
        val searchPlaceEdit = view?.findViewById<EditText>(R.id.searchPlaceEdit)
        val bgImageView = view?.findViewById<ImageView>(R.id.bgImageView)

        adapter = PlaceAdapter(this,viewModel.placeList)
        if (recyclerView != null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }

        if (searchPlaceEdit != null) {
            searchPlaceEdit.addTextChangedListener { editable ->
                val content = editable.toString()
                if (content.isNotEmpty()){
                    viewModel.searchPlaces(content)
                }else{
                    if (recyclerView != null) {
                        recyclerView.visibility = View.GONE
                    }
                    if (bgImageView != null) {
                        bgImageView.visibility = View.VISIBLE
                    }
                    viewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {
            result ->
            val places = result.getOrNull()
            if (places != null){
                if (recyclerView != null) {
                    recyclerView.visibility = View.VISIBLE
                }
                if (bgImageView != null) {
                    bgImageView.visibility = View.GONE
                }
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"unknown location",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

    }
}