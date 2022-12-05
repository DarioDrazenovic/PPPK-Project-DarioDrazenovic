package hr.algebra.personmanager.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) var _id: Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var picturePath: String? = null,
    var birthDate: LocalDate = LocalDate.now(),
    var onlineName: String? = null
    ) {
    override fun toString() = "$firstName $lastName"
}
