package hr.algebra.personmanager.dao

import androidx.room.*

@Dao
interface PersonDao {

    @Query("select * from people")
    fun getPeople(): MutableList<Person>

    @Query("select * from people where _id=:id")
    fun getPerson(id: Long): Person?

    @Insert
    fun insert(person: Person)

    @Update
    fun update(person: Person)

    @Delete
    fun delete(person: Person)
}