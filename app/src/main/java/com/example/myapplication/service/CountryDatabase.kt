package com.example.myapplication.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Country

@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDAO(): CountryDao

    // Farklı veya aynı zamanda farklı threadlerden veritabanımıza ulaşılmak istenirse conflict olabilir ...
    // Bu yüzden tek bir instance olmalıdır. --> Singleton mantığında olacak ...
    // Propertyler; volatile tanımlanırsa, diğer threadlere görünür hale gelir ... --> coroutines kullanılmasaydı, kullanmaya gerek kalmayacaktı ...
    // Bu sınıfı singleton yapma amacımız farklı threadlerde çalıştırmaktır ...
    companion object {

        @Volatile
        private var instance: CountryDatabase? = null // tek oluşturulacak olan obje ...

        private val lock = Any()

        // Burada instance var mı onu kontrol et.
        // varsa intance ver, yoksa intance'ı olustur ...
        // synronized --> bir instance'a iki farklı thread aynı anda ulaşmaya çalışırsa buna izin vermez ...
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }
        // databasebuilder içinde context kullanırken; uygulama yan çevrilir, fragment vs destroy olur diye context değilde, direk applicationContext(uygulamanın kendi contexti) kullandık ...
        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CountryDatabase::class.java,
            "countrydatabase"
        ).build()

    }
}