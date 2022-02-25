package com.example.android.stepapp.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.databinding.FragmentHistoryBinding
import com.example.android.stepapp.home.DayListAdapter
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentHistoryBinding = DataBindingUtil.inflate( inflater,R.layout.fragment_history, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = DayDatabase.getInstance(application).dayDatabaseDao
        val viewModelFactory = HistoryViewModelFactory(dataSource, application)
        viewModel= ViewModelProvider(this,viewModelFactory).get(HistoryViewModel::class.java)
        val adapter = DayListAdapter()
        val recyclerView = binding.recycleDayView
        recyclerView.adapter= adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val calendarView = binding.calendarView


        calendarView.setOnDateChangeListener { calendarView,y,m,d ->
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val selectedDate = sdf.format(Date(calendarView.date))
            Toast.makeText(requireContext(),selectedDate ,Toast.LENGTH_SHORT ).show()
        }


        viewModel.readAllData.observe(viewLifecycleOwner, Observer { day ->
            adapter.setData(day)
        })

        return binding.root
    }
}