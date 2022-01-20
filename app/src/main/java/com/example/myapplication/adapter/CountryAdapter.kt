package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCountryBinding
import com.example.myapplication.model.Country
import com.example.myapplication.view.FeedFragmentDirections

class CountryAdapter :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), CountryClickListener {

    private var countryList: List<Country> = mutableListOf()

    class CountryViewHolder(var binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemCountryBinding>(
            inflater,
            R.layout.item_country,
            parent,
            false
        )
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        val country = countryList[position]
        holder.binding.country = country
        holder.binding.listener = this

        /*
        holder.view.findViewById<AppCompatTextView>(R.id.tvName).text =
            country.countryName

        holder.view.findViewById<AppCompatTextView>(R.id.tvRegion).text =
            country.countryRegion

        holder.view.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
        */

        /*
        country.imageUrl?.let { imageUrl ->
            Log.e("sdfsd",R.id.ivCountryImage.toString())
            holder.view.findViewById<AppCompatImageView>(R.id.ivCountryImage).setImage(
                imageUrl, placeholderProgressBar(holder.view.context))
        }
        */

    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    // swipe edince listeyi günceller ...
    fun updateCountryList(newCountryList: List<Country>) {
        (countryList as ArrayList<Country>).clear()
        (countryList as ArrayList<Country>).addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uuid = v.findViewById<AppCompatTextView>(R.id.tvUuid).text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }

}