package com.example.myapplication.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Country
import com.example.myapplication.service.CountryAPIService
import com.example.myapplication.service.CountryDatabase
import com.example.myapplication.utils.CustomSharedPreferrences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferrences(getApplication())
    private var refreshtTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData() {
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshtTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }
    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDAO().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(), "Countries from SQLite", Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI() {
        countryLoading.value = true
        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread()) // asyncron şekilde yapacak ... --> arka planda çalışacak olan yeni bir threadde yapılır ...
                .observeOn(AndroidSchedulers.mainThread()) // nerede --> gözlemleme main threadde kullanıcı ara yüzünde yapılacak ...
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(countriesList: List<Country>) {
                        storeInSQLite(countriesList)
                        Toast.makeText(getApplication(), "Countries from API", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onError(e: Throwable) {
                        countryError.value = true
                        countryLoading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun showCountries(countriesList: List<Country>) {
        countries.value = countriesList
        countryError.value = false
        countryLoading.value = false
    }

    /**
     * toTypeArray --> listeyi tekil hale getirir ...
     */
    private fun storeInSQLite(list: List<Country>) {
        launch {
            val dao = CountryDatabase(getApplication()).countryDAO()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray()) // list -> individual
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i += 1
            }
            showCountries(list)
        }
        // nanoTime --> en hassas zaman işlemi
        customPreferences.saveTime(System.nanoTime())
    }

    /**
     * Fragmentlar yok olunca kolayca yok eder ...
     * Hafızayı daha verimli hale getirir ...
     */
   override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}