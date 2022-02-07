package com.example.android.stepapp.goal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.R
import com.example.android.stepapp.databinding.FragmentGoalsBinding
import com.example.android.stepapp.databinding.FragmentHomeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [GoalsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalsFragment : Fragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?)
    : View? {
        // Inflate the layout for this fragment
        val binding : FragmentGoalsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goals, container, false)



        return binding.root
    }


}