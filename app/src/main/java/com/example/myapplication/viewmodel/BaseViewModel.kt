package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Bu sınıfı tüm viewmodellarda coroutine'i rahat bir şekilde kullanabilmek için yazdık ...
 * Viewmodel yerine Androidviewmodel daha güvenli olur. Çünkü androidviewmodel applicationcontext alır
 * ve fragment destroy vs olsa dahi kopmalar yaşanmaz ...
 * Bu kısım sadece implements edileceği için abstract class olacaktır ...
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job() // bu iş arka planda çalıaşcak olan iş .
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main // önce işini yap sonra main thread'e dön anlamına gelir .

    /**
     * applicationcontext'te giderse bu iş iptal edilir ...
     */
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}