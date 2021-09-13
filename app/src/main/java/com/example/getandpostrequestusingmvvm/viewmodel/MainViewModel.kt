package com.example.getandpostrequestusingmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getandpostrequestusingmvvm.model.dataclass.commentsdataclass.CommentsDataClassItem
import com.example.getandpostrequestusingmvvm.model.dataclass.postsdataclass.AllPostDataClassItem
import com.example.getandpostrequestusingmvvm.model.dataclass.singlecommentsdataclass.SingleCommentsDataClass
import com.example.getandpostrequestusingmvvm.model.dataclass.singleuserdataclass.SingleUserDataClass
import com.example.getandpostrequestusingmvvm.model.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Create a view model class that will be used by the main activity, let it extend the ViewModel class
 * and pass The Repository class via it
 */
class MainViewModel(private val repository:Repository):ViewModel() {
    var variableGetSingleUserDataInMutableLiveData :MutableLiveData<Response<AllPostDataClassItem>> = MutableLiveData()
    var variableGetAllPostInMutableLiveData: MutableLiveData<Response<List<AllPostDataClassItem>>> = MutableLiveData()
    var variableGetCommentsInMutableLiveDataPerPostId: MutableLiveData<Response<List<CommentsDataClassItem>>> = MutableLiveData()

    var variableGetSingleCommentsInMutableLiveData : MutableLiveData<Response<CommentsDataClassItem>> = MutableLiveData()

    /**
     * Create a function that runs on a different thread while it get data from the repository
     *
     * On a new thread because, it might take few seconds for data to be accessed via the interface
     *
     * Before the data is then converted and then data is passed through the MainViewModel Class
     */
    fun functionGetSingleUserDataInMainViewModel(){
        viewModelScope.launch {
            /**
             * Get data from repository and store in Mutable live data, so that it can be observed in the main activity
             */
            val singleUserResponseInViewModelScope = repository.getSingleUserDataInRepository()
            variableGetSingleUserDataInMutableLiveData.value = singleUserResponseInViewModelScope
        }
    }

    fun functionGetAllPostInMainViewModel(){
        viewModelScope.launch {
            val allPostsInViewModelScope:Response<List<AllPostDataClassItem>> = repository.getAllPostsInRepository()
            variableGetAllPostInMutableLiveData.value = allPostsInViewModelScope


        }
    }

    fun functionGetCommentsInMainViewModelPerPostId(number:Int){
        viewModelScope.launch {
            val commentsInViewModelScopePerPostId = repository.getCommentsInRepositoryPerPostId(number)
            variableGetCommentsInMutableLiveDataPerPostId.value = commentsInViewModelScopePerPostId

        }
    }

    fun functionGetSingleCViewommentsInMainViewModel(){
        viewModelScope.launch {
            val singleCommentsInViewModelScope = repository.getSingleCommentsInRepository()
            variableGetSingleCommentsInMutableLiveData.value = singleCommentsInViewModelScope
        }
    }

    fun functionPushCommentsInMainViewModel(pushCommentsInMainViewModel:CommentsDataClassItem){
        viewModelScope.launch {
            val pushCommentsInViewModelScope = repository.pushCommentsInRepository(pushCommentsInMainViewModel)
            variableGetSingleCommentsInMutableLiveData.value = pushCommentsInViewModelScope
        }

    }

    fun functionPushPostInMainViewModel(pushPostInMainViewModel:AllPostDataClassItem){
        viewModelScope.launch {
            val pushPostsInViewModelScope = repository.pushPostInRepository(pushPostInMainViewModel)
            variableGetSingleUserDataInMutableLiveData.value = pushPostsInViewModelScope

        }
    }
}