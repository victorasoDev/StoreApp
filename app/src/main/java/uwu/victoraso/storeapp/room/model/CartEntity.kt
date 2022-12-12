package uwu.victoraso.storeapp.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines an [CartEntity]
 * It is a parent in a 1 to many relationship with [CartProductEntity]
 */
@Entity(
    tableName = "cart",
)
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "itemCount")
    val itemCount: Int = 0,
)
