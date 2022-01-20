package com.example.myapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CountryAdapter
import com.example.myapplication.databinding.FragmentFeedBinding
import com.example.myapplication.model.Country
import com.example.myapplication.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var binding: FragmentFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java) // *** dikkat
        viewModel.refreshData()
        countryAdapter = CountryAdapter()

        binding.rvCountryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCountryList.adapter = countryAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.rvCountryList.visibility = View.GONE
            binding.tvCountryError.visibility = View.GONE
            binding.pbCountryLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()

            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
        //val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(1)
        //Navigation.findNavController(view).navigate(action)
    }

    fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner) { countries ->
            countries?.let {
                binding.rvCountryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        }

        viewModel.countryError.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (error) {
                    binding.tvCountryError.visibility = View.VISIBLE
                } else {
                    binding.tvCountryError.visibility = View.GONE
                }
            }
        }

        viewModel.countryLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (loading) {
                    binding.pbCountryLoading.visibility = View.VISIBLE
                    binding.rvCountryList.visibility = View.GONE
                    binding.tvCountryError.visibility = View.GONE
                } else {
                    binding.pbCountryLoading.visibility = View.GONE
                }
            }
        }
    }
}