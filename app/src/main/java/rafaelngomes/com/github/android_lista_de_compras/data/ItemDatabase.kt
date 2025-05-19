package rafaelngomes.com.github.android_lista_de_compras.data

import androidx.room.Database
import androidx.room.RoomDatabase
import rafaelngomes.com.github.android_lista_de_compras.model.ItemModel


@Database(entities = [ItemModel::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {


    abstract fun itemDao(): ItemDao
}