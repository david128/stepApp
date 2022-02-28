package com.example.android.stepapp.historical

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.android.stepapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [HistoricalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoricalFragment : Fragment() {


    private val args : HistoricalFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("args",args.day.stepDate )
        Log.d("args",args.day.stepGoal.toString() )
        Log.d("args",args.day.stepGoalName )
        Log.d("args",args.day.stepCount.toString() )
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historical, container, false)
    }

}