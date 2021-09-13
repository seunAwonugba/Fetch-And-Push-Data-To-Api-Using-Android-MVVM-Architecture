package com.example.getandpostrequestusingmvvm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getandpostrequestusingmvvm.R
import com.example.getandpostrequestusingmvvm.connectivity.ConnectivityLiveData
import com.example.getandpostrequestusingmvvm.model.dataclass.commentsdataclass.CommentsDataClassItem
import com.example.getandpostrequestusingmvvm.model.repository.Repository
import com.example.getandpostrequestusingmvvm.view.adapter.CommentsAdapter
import com.example.getandpostrequestusingmvvm.viewmodel.MainViewModel
import com.example.getandpostrequestusingmvvm.viewmodel.MainViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CommentsActivity : AppCompatActivity() {
    /**
     * Create instance of view model, connectivityLivedata, connectivityText, PostAdapter, backButton,
     * Recyclerview, commentsAdapter, commentsButton, all edit textView
     */
    private lateinit var instanceOfMainViewModel: MainViewModel
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var connectivityText: TextView
    private lateinit var instanceOfRecyclerView: RecyclerView
    private lateinit var instanceOfCommentsAdapter: CommentsAdapter
    private lateinit var instanceOfPostFromPostActivityTextView: TextView
    private lateinit var instanceOfBackButton: ImageView
    private lateinit var instanceOfNameEditTextView: TextInputEditText
    private lateinit var instanceOfBodyEditTextView: TextInputEditText
    private lateinit var instanceOfPostCommentsButton: Button
    private lateinit var instanceOfTextInputLayout: TextInputLayout
    private lateinit var instanceOfTextInputLayout2: TextInputLayout

    /**
     * Create an instance Of commentsAdapterArrayList, i.e an instance of the array list passed in the
     * commentsAdapter constructor, so you can also pass input comments as an array and display in
     * the recyclerview
     */
    private lateinit var instanceOfCommentsArrayList: CommentsDataClassItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        /**
         * Grab title and Id of post sent in by intent from main/post activity/adapter
         */
        val passedPostFromPostActivity = intent.getStringExtra("TITLE")
        val passedPostIdFromPostActivity = intent.getStringExtra("POSTID")
        /**
         * Convert the postIdPassedFromPostActivity or Adapter to string cos it came as string?
         *
         */
        val convertPostIdFromNullStringToString = passedPostIdFromPostActivity.toString()

        /**
         * Grab Id of postsTextView, backButton in comments activity, Recyclerview in comments
         * activity
         */
        instanceOfPostFromPostActivityTextView = findViewById(R.id.postFromPostActivityId)
        instanceOfBackButton = findViewById(R.id.backButtonId)
        instanceOfRecyclerView = findViewById(R.id.commentsRecyclerViewId)
        instanceOfNameEditTextView = findViewById(R.id.commentNameId)
        instanceOfBodyEditTextView = findViewById(R.id.commentBodyId)
        instanceOfPostCommentsButton = findViewById(R.id.postCommentsButtonInCommentsActivityId)
        instanceOfTextInputLayout = findViewById(R.id.textInputLayout)
        instanceOfTextInputLayout2 = findViewById(R.id.textInputLayout2)


        /**
         * Set up comments recyclerview
         */
        instanceOfRecyclerView.layoutManager = LinearLayoutManager(this)
        instanceOfRecyclerView.setHasFixedSize(true)

        /**
         * Assign the created post text view space to the title text coming from postActivity
         */
        instanceOfPostFromPostActivityTextView.text = passedPostFromPostActivity

        /**
         * Set onClickListener on the back button
         */
        instanceOfBackButton.setOnClickListener {
            goBackFunction()
        }

        /**
         * Create an instance of Repository
         */
        val instanceOfRepository= Repository()

        /**
         * Grab view by ID for connectivityLivedata, connectivityText, recyclerView
         */
        connectivityLiveData = ConnectivityLiveData(application)
        connectivityText = findViewById(R.id.commentsConnectivityTextId)

        /**
         * Invite connectivityLivedata to observe network connection
         */
        connectivityLiveData.observe(this, { isAvailable ->
            when (isAvailable) {
                true -> {
                    connectivityText.visibility = View.GONE
                    instanceOfRecyclerView.visibility = View.VISIBLE
                    instanceOfNameEditTextView.visibility = View.VISIBLE
                    instanceOfBodyEditTextView.visibility = View.VISIBLE
                    instanceOfPostCommentsButton.visibility = View.VISIBLE
                    instanceOfTextInputLayout2.visibility = View.VISIBLE
                    instanceOfTextInputLayout.visibility = View.VISIBLE
                }
                false -> {
                    connectivityText.visibility = View.VISIBLE
                    instanceOfRecyclerView.visibility = View.GONE
                    instanceOfNameEditTextView.visibility = View.GONE
                    instanceOfBodyEditTextView.visibility = View.GONE
                    instanceOfPostCommentsButton.visibility = View.GONE
                    instanceOfTextInputLayout2.visibility = View.GONE
                    instanceOfTextInputLayout.visibility = View.GONE
                }
            }
        })

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
         * call functionGetCommentsInMainViewModelPerPostId from MainViewModel, the function helps to store data in
         * mutable live data, so that the data can be observed in the comments
         */
        instanceOfMainViewModel.functionGetCommentsInMainViewModelPerPostId(Integer.parseInt(convertPostIdFromNullStringToString))
        /**
         * Store the data received or observed from mutable live data as response from Api
         */
        instanceOfMainViewModel.variableGetCommentsInMutableLiveDataPerPostId.observe(
            this, { apiCommentsResponseInMutableLiveDataPerPostId ->
                if (apiCommentsResponseInMutableLiveDataPerPostId.isSuccessful){
                    instanceOfCommentsAdapter = CommentsAdapter(
                        apiCommentsResponseInMutableLiveDataPerPostId.body() as ArrayList<CommentsDataClassItem>
                    )
                    instanceOfCommentsAdapter.notifyDataSetChanged()
                    instanceOfRecyclerView.adapter = instanceOfCommentsAdapter
                }
                else{
                    Log.d("COMMENTS", apiCommentsResponseInMutableLiveDataPerPostId.errorBody().toString())
                    Log.d("COMMENTS", apiCommentsResponseInMutableLiveDataPerPostId.code().toString())
                }

            }
        )

        /**
         * Post comments from comments activity
         */
        instanceOfPostCommentsButton.setOnClickListener {
            postToApiOnButtonClick()
            functionClearText()
        }

        /**
         * Using addTextChangeListener to enable post button when input fields are not empty
         */

        instanceOfBodyEditTextView.addTextChangedListener(addCommentsOnButtonClick)
        instanceOfNameEditTextView.addTextChangedListener(addCommentsOnButtonClick)

    }

    /**
     * This function grabs input from name and comments textview, the generate the other data randomly
     * Equates it to the data in the data class
     *
     * calls on the arraylist variable in the adapter then the data inside the adapter
     *
     * passes it to the recyclerview for display
     */

    private fun postToApiOnButtonClick() {

        val generatePostId = (0..10000).random()
        val generatedId = (0..10000000).random()
        val grabbedNameInComments = instanceOfNameEditTextView.text.toString()
        val defaultEmail = "seunawonugba@gmail.com"
        val grabbedBodyInComments = instanceOfBodyEditTextView.text.toString()

        instanceOfCommentsArrayList = CommentsDataClassItem(grabbedBodyInComments, defaultEmail, generatedId, grabbedNameInComments,generatePostId)
        instanceOfCommentsAdapter.commentsArrayList.add(instanceOfCommentsArrayList)
        instanceOfCommentsAdapter.notifyDataSetChanged()
        instanceOfRecyclerView.adapter = instanceOfCommentsAdapter

        val newCommentInput = instanceOfCommentsArrayList
        instanceOfMainViewModel.functionPushCommentsInMainViewModel(newCommentInput)
        instanceOfMainViewModel.variableGetSingleCommentsInMutableLiveData.observe(
            this,  { sendCommentsToApi->
                if (sendCommentsToApi.isSuccessful){
                    Log.d("POSTCOMMENTS", "Body: ${sendCommentsToApi.body().toString()}")
                    Log.d("POSTCOMMENTS", "Code: ${sendCommentsToApi.code()}")
                    Log.d("POSTCOMMENTS", "Message: ${sendCommentsToApi.message()}")
                }

            }
        )
    }

    private fun goBackFunction() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun functionClearText() {
        instanceOfNameEditTextView.text = null
        instanceOfBodyEditTextView.text = null
    }

    private val addCommentsOnButtonClick: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val grabbedBodyInComments: String = instanceOfBodyEditTextView.text.toString().trim()
            val grabbedNameInComments: String = instanceOfNameEditTextView.text.toString().trim()
            instanceOfPostCommentsButton.isEnabled = grabbedBodyInComments.isNotEmpty() && grabbedNameInComments.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {}
    }

}