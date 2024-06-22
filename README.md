**Изучение Room library. Запросы в database (CRUD - Create, read, update and delete in database).**

![logs](https://github.com/MishaNikolaev/LearningRoomDataBase/blob/master/app_screen.jpg)

**Основные моменты для запоминания:**

Бибилотека rooom в gradle:
``` Kotlin
implementation("androidx.room:room-ktx:2.5.1") //room
kapt("androidx.room:room-compiler:2.5.1") //room annotations
```
Для ViewModel: 
``` Kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0")
```

**Создал папку data и в ней интерфейс Dao (Data access object)** для доступа к данным
Dao может быть как интерфейс, так и абстрактный класс. Там находится описание методов.
Методы эти трудоёмкие и их нужно использовать не в основном потоке. Применяем suspend fun.
``` Kotlin
@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(nameEntity: NameEntity)

    @Delete
    suspend fun deleteItem(nameEntity: NameEntity)

    @Query("SELECT * FROM tableEntity")
    fun getAllItems() : Flow<List<NameEntity>>

}
```

Создаём класс **NameEntity** для того, чтобы бибилиотека room понимала, что на основе этого класса нужно создать таблицу.
``` Kotlin
@Entity(tableName = "tableEntity") 

data class NameEntity(
    @PrimaryKey(autoGenerate = true) 
    /* the room library will give us a unique identifier
    in the form of numbers like: 0,1,2.. */
    val id : Int? = null,
    val name: String
)
```

Чтобы room понимала, что нужно создать уникальный идентификатор.
``` Kotlin
@PrimaryKey(autoGenerate = true)
```

Создание базы данных: 

``` Kotlin
@Database(
    entities = [
        NameEntity::class
    ],
    version = 1
)
abstract class MainDataBase : RoomDatabase() {
    abstract val dao: Dao
    companion object{
        fun createDataBase(context: Context) : MainDataBase{
            return Room.databaseBuilder(
             context,
                MainDataBase::class.java,
                "test.db"
            ).build()
        }
    }
}
```

Инициализация базы данных:
С помощью функции lazy, если инстанции нет, то она будет создана:

``` Kotlin
class BaseApplication : Application() {
    val dataBase by lazy{
        MainDataBase.createDataBase(this)
    }
}
```

Переопределение suspend функций из Dao в ViewModel:
``` Kotlin
class MainViewModel(val dataBase: MainDataBase) : ViewModel() {

    val itemsList = dataBase.dao.getAllItems()
    val newText = mutableStateOf("")
    var nameEntity: NameEntity? = null

    fun insertItem() = viewModelScope.launch {
        val nameItem = nameEntity?.copy(name = newText.value) ?: NameEntity(name = newText.value)
        dataBase.dao.insertItem(nameItem)
        nameEntity = null
        newText.value = ""
    }

    fun deleteItem(item: NameEntity) = viewModelScope.launch {
        dataBase.dao.deleteItem(item)
    }

    //constructor
    companion object{
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress ("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as BaseApplication).dataBase
                return MainViewModel(database) as T
            }
        }
    }
}
```
