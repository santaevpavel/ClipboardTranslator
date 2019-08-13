package ru.santaev.clipboardtranslator.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.santaev.clipboardtranslator.db.entity.LanguageContract.CODE
import ru.santaev.clipboardtranslator.db.entity.LanguageContract.NAME
import java.io.Serializable

@Entity(
        tableName = LanguageContract.TABLE_NAME,
        indices = arrayOf(Index(value = *arrayOf(NAME), unique = true))
)
class Language(
        @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo(name = NAME) var name: String?,
        @ColumnInfo(name = CODE) var code: String?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val language = other as Language?

        if (id != language!!.id) return false
        return if (name != language.name) false else code == language.code
    }

    override fun hashCode(): Int {
        var result = (id xor id.ushr(32)).toInt()
        result = 31 * result + name!!.hashCode()
        result = 31 * result + code!!.hashCode()
        return result
    }
}

