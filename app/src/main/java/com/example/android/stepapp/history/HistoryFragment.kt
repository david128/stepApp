package com.example.android.stepapp.history

import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.databinding.FragmentHistoryBinding
import com.example.android.stepapp.home.DayListAdapter
import java.util.*
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays
import com.example.android.stepapp.DateToString
import java.text.SimpleDateFormat


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


        val calendarView=binding.calendarView
        val dts = DateToString()
        val calendar = Calendar.getInstance()


        calendarView.setOnDateChangeListener { cv,y,m,d ->
            calendar.set(y,m,d)
            calendarView.date = calendar.timeInMillis
            val selectedDate = dts.toSimpleString(Date(cv.date))
            Toast.makeText(requireContext(),selectedDate ,Toast.LENGTH_SHORT ).show()
            //get day
        }




        return binding.root
    }
}