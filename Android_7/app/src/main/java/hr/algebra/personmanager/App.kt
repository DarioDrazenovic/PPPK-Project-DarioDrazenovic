package hr.algebra.personmanager

import android.app.Application
import hr.algebra.personmanager.dao.PeopleDatabase
import hr.algebra.personmanager.dao.PersonDao

class App : Application() {

    private lateinit var personDao: PersonDao
    fun getPersonDao() = personDao

    override fun onCreate() {
        super.onCreate()
        var db = PeopleDatabase.getInstance(this)
        personDao = db.personDao()
    }
}