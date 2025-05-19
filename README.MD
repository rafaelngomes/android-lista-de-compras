# 🛒 Lista de Compras

## 📌 Sobre o Projeto

Um aplicativo de lista de compras desenvolvido em **Kotlin**.


---

## ⚙️ Funcionalidades

- ✅ Adicionar novos itens à lista de compras
- 🗑️ Remover itens da lista de compras

---

## 📸 Telas do Projeto
![Captura de tela 2025-05-19 102633](https://github.com/user-attachments/assets/520d0fbd-48a5-422f-82a5-b0ffc031!cbed)


-Lista de compras
![Captura de tela 2025-05-19 110104](https://github.com/user-attachments/assets/7c1d6727-aa33-4650-bd70-e8ff3a57a152)


- Lista de compras adicionando itens
![Captura de tela 2025-05-18 144139](https://github.com/user-attachments/assets/4a956798-1502-454b-9a37-fc25f33324f1)



- Removendo arroz
![Captura de tela 2025-05-18 144150](https://github.com/user-attachments/assets/a1cd64f3-0c8f-4696-8bf2-1be3adf3e43e)



---


## 🧠 Conceitos Aplicados

- **RecyclerView**: utilizado para exibir listas, permitindo criar listas dinâmicas, personalizadas e recicláveis, o que significa que ele cria e destrói a lista de acordo com o scroll da tela, sendoo um ciclo.

- **Room Database**:  permite manipulação nos dados da aplicação de forma simples,  segura e estruturada de dados persistentes. Room cuida da criação do banco de dados, acesso aos dados via DAOs (Data Access Objects) e facilita a integração com o ciclo de vida do Android.

- **ViewModel**:  armazena e gerencia dados da UI de forma isolada. Garante que os dados sobrevivam a mudanças de configuração, como rotação de tela.

- **Coroutines**: Para a realização de chamadas assíncronas, sem travar a aplicação

---

## 🗄️ Banco de Dados

### 📌 `ItemModel.kt`

É uma data class que representa uma tabela no banco de dados, o que pode ser denotado pela notação `@Entity`. Cada instância dessa classe representa uma linha na tabela do banco de dados.

@Entity
//entity -> uma tabela, uma entidade no banco de dados.
data class ItemModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
```

### 📌 `Item DAO`
É uma interface que implementa e gerencia os métodos (select, insert e delete) do banco de dados relacionados ao ItemModel.```kotlin

@Dao
interface ItemDao {
    @Query("SELECT * FROM ItemModel")
    fun getAll(): LiveData<List<ItemModel>>

    @Insert
    fun insert(item: ItemModel)

    @Delete
    fun delete(item: ItemModel)
}
```
### 📌`Item database`
É uma classe abstrata que faz a conexão do banco de dados com a aplicação.

@Database(entities = [ItemModel::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
```
## `View Model e adapters`
A ViewModel é uma classe que gerencia e prepara os dados para a interface, isolando a lógica da UI. Já o Adapter é responsável por adaptar os dados da lista para a visualização no RecyclerView.
### 📌`ItemViewModel`
Contém funções que manipulam os itens do banco de dados.

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val itemDao: ItemDao
    val itemsLiveData: LiveData<List<ItemModel>>

    init {
        val database = Room.databaseBuilder(
            getApplication(),
            ItemDatabase::class.java,
            "items_database"
        ).build()
        itemDao = database.itemDao()
        itemsLiveData = itemDao.getAll()
    }

    fun addItem(item: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = ItemModel(name = item)
            itemDao.insert(newItem)
        }
    }

    fun removeItem(item: ItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }
}
```

### 📌`itemAdapter`
É a classe que fornece os dados e cria cada item na lista. É ligada ao RecyclerView, adaptando os dados vindos do banco para serem exibidos

class ItemsAdapter(private val onItemRemoved: (ItemModel) -> Unit) :
RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    
    private var items = listOf<ItemModel>()
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textViewItem)
        val button = view.findViewById<ImageButton>(R.id.imageButton)
        
        fun bind(item: ItemModel) {
//            textView.text = item.name
            button.setOnClickListener {
                onItemRemoved(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }
    
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}
```

## 📦 Dependências e plugins:
### 🔌 Plugins
#### kotlin("kapt"): Usado para habilitar o processamento de anotações, necessário para o Room.
### 📚 Bibliotecas
#### RecyclerView: Responsavel por criar listas dinamicas e lidar com páginação automatica dentro do app

implementation("androidx.recyclerview:recyclerview:1.3.2")
```
#### Room Runtime: Biblioteca principal do Room para acessar e manipular o banco de dados SQLite

implementation("androidx.room:room-runtime:2.4.1")
implementation("androidx.room:room-ktx:2.4.1")
kapt("androidx.room:room-compiler:2.4.1")
``` 
#### Lifecycle - LiveData: Para lidar com "dados vivos", ou seja, para lidar com mudanças que ocorrem no bancod e dados da aplicaçãoe m tempo real

implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
```

#### AppCompat: Para a retrocompatibilidade (capacidade de se adaptar as versões anteriores)

implementation("androidx.appcompat:appcompat:1.4.1")
```
#### Activity KTX: manipulação de activities

implementation("androidx.activity:activity-ktx:1.7.0")
```
#### Coroutines: Para fazer chamadas assíncronas sem bloquear a UI

implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
```

