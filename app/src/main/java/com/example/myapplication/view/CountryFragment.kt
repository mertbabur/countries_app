package com.example.myapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.databinding.FragmentCountryBinding
import com.example.myapplication.viewmodel.CountryViewModel

class CountryFragment : Fragment() {

    private lateinit var viewModel: CountryViewModel
    private lateinit var binding: FragmentCountryBinding
    private var countryUuid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            countryUuid = CountryFragmentArgs.fromBundle(bundle).countryUuid
        }

        viewModel.getDataFromRoom(countryUuid)

        initObserver()
    }

    private fun initObserver() {
        viewModel.countryLiveData.observe(viewLifecycleOwner) { country ->
            country?.let {
                with(binding){
                    selectedCountry = country
                }
            }
        }
    }

}