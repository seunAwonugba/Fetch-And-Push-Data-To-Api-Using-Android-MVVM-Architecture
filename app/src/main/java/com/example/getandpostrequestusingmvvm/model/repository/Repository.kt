package com.example.getandpostrequestusingmvvm.model.repository

import com.example.getandpostrequestusingmvvm.model.dataclass.commentsdataclass.CommentsDataClassItem
import com.example.getandpostrequestusingmvvm.model.dataclass.postsdataclass.AllPostDataClassItem
import com.example.getandpostrequestusingmvvm.model.dataclass.singlecommentsdataclass.SingleCommentsDataClass
import com.example.getandpostrequestusingmvvm.model.dataclass.singleuserdataclass.SingleUserDataClass
import com.example.getandpostrequestusingmvvm.model.gsonconverter.GsonConverter
import retrofit2.Response

/**
 * Create a class to represent our repository, it will use the suspend fun keyword to return the data in the dataclass
 *
 * in returning the data in data class, we first get the data in the interface using the suspend function in the interface
 *
 * The data is stored in the passApiInterface that was lazily initialised and we can access all that
 *
 * in this class with the help of the object class, the we simply return the data
 *
 *
 */
class Repository {
    suspend fun getSingleUserDataInRepository():Response<AllPostDataClassItem>{
        return GsonConverter.passApiInterface.getDataFromInterfaceForSingleUser()
    }

    suspend fun getAllPostsInRepository():Response<List<AllPostDataClassItem>>{
        return GsonConverter.passApiInterface.getDataFromInterfaceForAllPosts()
    }

    suspend fun getCommentsInRepositoryPerPostId(number:Int):Response<List<CommentsDataClassItem>>{
        return GsonConverter.passApiInterface.getCommentsInInterfacePerPostId(number)
    }

    suspend fun getSingleCommentsInRepository():Response<CommentsDataClassItem>{
        return  GsonConverter.passApiInterface.getSingleCommentsInInterface()
    }

    /**
     * Function to post comments in repository
     */

    suspend fun pushCommentsInRepository(commentsInRepository: CommentsDataClassItem):Response<CommentsDataClassItem>{
        return GsonConverter.passApiInterface.pushCommentsToCommentsApi(commentsInRepository)
    }

    /**
     * Function to push post to API
     */
    suspend fun pushPostInRepository(postInRepository: AllPostDataClassItem):Response<AllPostDataClassItem>{
        return GsonConverter.passApiInterface.pushPostToPostApi(postInRepository)
    }
}