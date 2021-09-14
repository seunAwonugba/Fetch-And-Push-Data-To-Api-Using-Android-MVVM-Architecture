package com.example.getandpostrequestusingmvvm.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getandpostrequestusingmvvm.R
import com.example.getandpostrequestusingmvvm.connectivity.ConnectivityLiveData
import com.example.getandpostrequestusingmvvm.model.dataclass.postsdataclass.AllPostDataClassItem
import com.example.getandpostrequestusingmvvm.model.repository.Repository
import com.example.getandpostrequestusingmvvm.view.adapter.PostAdapter
import com.example.getandpostrequestusingmvvm.view.search.SearchArrayList.dataComingFromApiForSearchView
import com.example.getandpostrequestusingmvvm.viewmodel.MainViewModel
import com.example.getandpostrequestusingmvvm.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    /**
     * Create instance of view model, connectivityLivedata, connectivityText, PostAdapter
     */
    private lateinit var instanceOfMainViewModel: MainViewModel
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var connectivityText: TextView
    private lateinit var instanceOfRecyclerView: RecyclerView
    private lateinit var instanceOfAllPostAdapter: PostAdapter
    private lateinit var instanceOfFloatingButton: FloatingActionButton
    private lateinit var instanceOfPostDataClass : AllPostDataClassItem

    /**
     * Create an instance of Repository
     */
    private val instanceOfRepository= Repository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /**
         * Grab view by ID for connectivityLivedata, connectivityText, recyclerView
         */
        connectivityLiveData = ConnectivityLiveData(application)
        connectivityText = findViewById(R.id.connectivityTextId)
        instanceOfRecyclerView = findViewById(R.id.recyclerViewId)
        instanceOfFloatingButton = findViewById(R.id.floatingActionButtonId)


        /**
         * Initialise the adapter
         */
        instanceOfAllPostAdapter = PostAdapter(arrayListOf(),this)

        /**
         * Set up the recyclerview
         */
        instanceOfRecyclerView.layoutManager = LinearLayoutManager(this)
        instanceOfRecyclerView.setHasFixedSize(true)


        /**
         * Invite connectivityLivedata to observe network connection
         */
        connectivityLiveData.observe(this, { isAvailable ->
            when (isAvailable) {
                true -> {
                    connectivityText.visibility = View.GONE
                    instanceOfRecyclerView.visibility = View.VISIBLE
                }
                false -> {
                    connectivityText.visibility = View.VISIBLE
                    instanceOfRecyclerView.visibility = View.GONE
                }
            }
        })

        if(Intent.ACTION_SEARCH == intent.action){
            handleIntent(intent)
        }
        else {
            /**
             * Create an instance of view model factory and pass the instance of repository as a parameter
             */
            val instanceOfViewModelFactory = MainViewModelFactory(instanceOfRepository)
            /**
             * Initialise the viewModel, assign the viewModelFactory to it, and pass two parameter via it
             *
             * the owner and the instance of viewModelFactory, that's how we have successfully passed the repository
             *
             * to the viewModel
             */
            instanceOfMainViewModel =
                ViewModelProvider(this, instanceOfViewModelFactory).get(MainViewModel::class.java)
            /**
             * call functionGetSingleUserDataInMainViewModel from Main ViewModel, the function helps to store data in
             * mutable live data, so that the data can be observed in the main activity
             *
             * Store the data received or observed from mutable live data as response from Api
             *
             * call functionGetAllPostInMainViewModel from mainViewModel
             */
            instanceOfMainViewModel.functionGetAllPostInMainViewModel()
            instanceOfMainViewModel.variableGetAllPostInMutableLiveData.observe(
                this, { apiResponseInMutableLiveData ->
                    if (apiResponseInMutableLiveData.isSuccessful) {
                        Log.d("TAG", apiResponseInMutableLiveData.body().toString())
                        dataComingFromApiForSearchView = apiResponseInMutableLiveData.body()!!

                        instanceOfAllPostAdapter =
                            apiResponseInMutableLiveData.body()?.let { PostAdapter(it as ArrayList<AllPostDataClassItem>, this) }!!
                        instanceOfAllPostAdapter.notifyDataSetChanged()
                        instanceOfRecyclerView.adapter = instanceOfAllPostAdapter
                    } else {
                        Log.d("TAG", apiResponseInMutableLiveData.errorBody().toString())
                    }
                }
            )
        }

        instanceOfFloatingButton.setOnClickListener { toAddNewPostActivity() }
        receiveDataPassedFromShareButton()

    }

    private fun toAddNewPostActivity() {
        val intent = Intent(this, AddNewPostActivity::class.java )
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }

    }

    private fun doMySearch(query: String) {
        val filtered = dataComingFromApiForSearchView.filter { it.body.contains(query,false) || it.title.contains(query,false) }
        instanceOfAllPostAdapter = PostAdapter(filtered as ArrayList<AllPostDataClassItem>, this)
        instanceOfAllPostAdapter.notifyDataSetChanged()
        instanceOfRecyclerView.adapter = instanceOfAllPostAdapter

    }

    /**
     * To display the SearchView in the app bar, inflate the search xml
     * in the onCreateOptionsMenu() method
     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.searchViewId)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    private fun receiveDataPassedFromShareButton(){
        val receivedUserId = intent.getStringExtra("PASSED_USER_ID")?.toInt()
        val receivedId = intent.getStringExtra("PASSED_ID")?.toInt()
        val receivedTitle = intent.getStringExtra("PASSED_TITLE")
        val receivedBody = intent.getStringExtra("PASSED_BODY")


        if(receivedBody != null){
            if(receivedTitle != null){
                if(receivedId != null){
                    if(receivedUserId != null){
                        /**
                         * Create an instance of view model factory and pass the instance of repository as a parameter
                         */
                        val instanceOfViewModelFactory = MainViewModelFactory(instanceOfRepository)

                        instanceOfMainViewModel =
                            ViewModelProvider(this, instanceOfViewModelFactory).get(MainViewModel::class.java)

                        instanceOfPostDataClass = AllPostDataClassItem(receivedBody, receivedId, receivedTitle, receivedUserId)
                        instanceOfMainViewModel.functionPushPostInMainViewModel(instanceOfPostDataClass)
                        instanceOfMainViewModel.variableGetSingleUserDataInMutableLiveData.observe(
                            this,  { sendPostToApiFromDataReceivedInAddNewPostActivity->
                                if(sendPostToApiFromDataReceivedInAddNewPostActivity.isSuccessful){
                                    Log.d("TAG", sendPostToApiFromDataReceivedInAddNewPostActivity.body().toString())
                                    Log.d("TAG", sendPostToApiFromDataReceivedInAddNewPostActivity.code().toString())
                                    Log.d("TAG", sendPostToApiFromDataReceivedInAddNewPostActivity.message().toString())

                                   instanceOfAllPostAdapter.postArrayList.add(instanceOfPostDataClass)
                                    instanceOfAllPostAdapter = PostAdapter(instanceOfAllPostAdapter.postArrayList,this)

                                    instanceOfAllPostAdapter.notifyDataSetChanged()
                                    instanceOfRecyclerView.adapter = instanceOfAllPostAdapter
                                }
                            }
                        )

                    }

                }

            }
        }

    }
}