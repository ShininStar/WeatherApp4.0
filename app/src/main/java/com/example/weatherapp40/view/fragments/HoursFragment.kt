package com.example.weatherapp40.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp40.databinding.FragmentHoursBinding
import com.example.weatherapp40.view.adapters.WeatherHourAdapter
import com.example.weatherapp40.viewmodel.MainViewModel

class HoursFragment : Fragment() {

    //заоплняем погоду по часам через WeatherHoursAdapter
    private var _binding: FragmentHoursBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeatherHourAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() = with(binding) {
        adapter = WeatherHourAdapter()
        rcViewHours.layoutManager = LinearLayoutManager(activity)
        rcViewHours.adapter = adapter
    }

    private fun observe() {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            adapter.submitList(it.forecast.forecastday[0].hour)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}