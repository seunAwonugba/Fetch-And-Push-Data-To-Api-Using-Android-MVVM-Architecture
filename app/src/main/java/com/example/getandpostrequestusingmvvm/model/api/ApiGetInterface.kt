package com.example.getandpostrequestusingmvvm.model.api

import com.example.getandpostrequestusingmvvm.model.dataclass.commentsdataclass.CommentsDataClassItem
import com.example.getandpostrequestusingmvvm.model.dataclass.postsdataclass.AllPostDataClassItem
import com.example.getandpostrequestusingmvvm.model.dataclass.singlecommentsdataclass.SingleCommentsDataClass
import com.example.getandpostrequestusingmvvm.model.dataclass.singleuserdataclass.SingleUserDataClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiGetInterface {
    /**
     * Get data from EndPoint , using a suspend fun because i will be using coroutine, the suspend
     * function should return the response data class
     */
    @GET("posts/1")
    suspend fun getDataFromInterfaceForSingleUser(): Response<AllPostDataClassItem>

    @GET("posts")
    suspend fun getDataFromInterfaceForAllPosts():Response<List<AllPostDataClassItem>>

    @GET("posts/{postId}/comments")
    suspend fun getCommentsInInterfacePerPostId(@Path("postId", encoded = true)number:Int):Response<List<CommentsDataClassItem>>

    /**
     * Get data for single comment
     */
    @GET("comments/1")
    suspend fun getSingleCommentsInInterface(): Response<CommentsDataClassItem>

    /**
     * Post comments endpoint
     */
    @POST("comments")
    suspend fun pushCommentsToCommentsApi(@Body commentsBodyAnnotation:CommentsDataClassItem):Response<CommentsDataClassItem>

    /**
     * Share post endpoint
     */
    @POST("posts")
    suspend fun pushPostToPostApi(@Body postBodyAnnotation:AllPostDataClassItem):Response<AllPostDataClassItem>
}