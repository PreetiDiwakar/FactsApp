# WiproFactsApp


An android app to  demonstrate offline caching capabilities offered by JetPack Compose.

The <a href = "https://developer.android.com/jetpack/guide"> Jetpack article</a> describes a way to provide data from a web service or retrieve data from an offline storage(if available). This repository contains the files required to make an application which can demonstrate such capabilities.


The goal here is to minimize the changes in the user-expereince. Important things to be noted:
<ul>
  <li> For the first time, when the application is started, the data is fetched from the webservice(restAPI, AWS, etc) in a background thread.</li>
  <li> As soon as the data fetch is done, all the data is stored in the local database, using the ROOM persistence library</li>
  <li> Parallelly, the data is exposed and shown in the activity/ fragment.</li>
  <li> If any change is there in the data, then the whole view is not refreshed, rather it is fetched in the background thread, laterwards the new data is replaced with the old data</li>
</ul>


The app follows the <a href = "https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjYuM7Xv8nyAhVMAHIKHSOjDr4QFnoECAYQAQ&url=https%3A%2F%2Fdeveloper.android.com%2Ftopic%2Flibraries%2Farchitecture%2Fviewmodel&usg=AOvVaw3f_7HpGuQps9xX6BXFMqhB" > MVVM </a> architecture.

The core part of this application is the `NetworkBoundResource.kt` file, where the magic happens.

##### Code
```kotlin
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}
```

<ol>
  <li>The above code first checks if there is any requirement for fetching the data or not.</li>.
  <ul>
    <li>If it is required to fetch the data, then it is emitted.</li> 
    <li>Else just look into the map</li>
  </ul>
  <li> Kotlin Flow is used here</li>
  </ol>
 <br></br>
  

# Application
This repository contains code for an android application, which basically shows a list of data fetched from a random API generator, using `RetrofitAPI` using which APIs are converted into callable objects. 

The following data are required to be fetched  and shown in the activity.

##### Code

```kotlin
@Entity(tableName = "facts")
data class Facts(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Nullable
    @ColumnInfo(name = "fact")
    var fact: String?,
    @Nullable
    @ColumnInfo(name = "title")
    val title: String?,
    @Nullable
    @ColumnInfo(name = "imageHref")
    val imageHref: String?,
    @Nullable
    @ColumnInfo(name = "description")
    val description: String?
)
```

A `repository` will be used. Repository pattern is one of the design patterns that available out there.A Repository is defined as a collection of domain objects that reside in the memory.


`DAO` for the API path

##### Code

```kotlin
@Dao
interface FactsDao {
    @Query("SELECT * FROM facts")
    fun getAllFacts(): Flow<List<Facts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFacts(facts: List<Facts>)

        @Query("DELETE FROM facts")
    suspend fun deleteAllFacts()
}
```

`Repository` class to centralize the data access

##### Code

```kotlin
class FactsRepository @Inject constructor(
    private val api: FactsAPI,
    private val db: FactsDatabase
) {
    private val factsDao = db.factsDao()
    private var title: String = "Facts"
    @NotNull
    fun getFacts() = networkBoundResource(
        query = {
            factsDao.getAllFacts()
        },
        fetch = {
            title = api.getFactsList().title
            api.getFactsList().results
        },
        saveFetchResult = { FactList ->
            db.withTransaction {
                factsDao.deleteAllFacts()
                val list: MutableList<Facts> = ArrayList()
                for(fact in FactList){
                    if(fact.title != null){
                        fact.fact = title
                        list.add(fact)
                    }
                }
                factsDao.insertFacts(list)
            }
        }
    )

}
```

This thing has to be implemented in a `viewModel` from which data will be exposed on a view or a fragment. 
The view model can get data from the repository by observing it's live data.


##### Code

```kotlin

@HiltViewModel
class FactsViewModel @Inject constructor(
    repository: FactsRepository
) : ViewModel() {
    val facts = repository.getFacts().asLiveData()
}
```
Adding a repository between the data source and a view is recommended by Android, as it seperates the view, so that focus can be put seperately on increasing the UI of app and the database. Moreover, the repository helps by centralising the data access, which directly reduces the boilerplate code.

