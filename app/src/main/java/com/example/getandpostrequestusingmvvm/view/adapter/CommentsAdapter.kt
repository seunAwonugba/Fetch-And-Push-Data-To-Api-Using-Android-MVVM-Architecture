package com.example.getandpostrequestusingmvvm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getandpostrequestusingmvvm.R
import com.example.getandpostrequestusingmvvm.model.dataclass.commentsdataclass.CommentsDataClassItem

class CommentsAdapter(var commentsArrayList: ArrayList<CommentsDataClassItem>):RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {
    inner class MyViewHolder(var myView:View):RecyclerView.ViewHolder(myView) {
        var instanceOfCommentsName: TextView
        var instanceOfCommentsBody: TextView

        init {
            instanceOfCommentsName = myView.findViewById(R.id.commentsNameTextViewId)
            instanceOfCommentsBody = myView.findViewById(R.id.commentsBodyTextViewId)
        }

        fun bindingFunction(bindingFunctionList:CommentsDataClassItem){
            instanceOfCommentsName.text = bindingFunctionList.name
            instanceOfCommentsBody.text = bindingFunctionList.body
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflatingVariable = LayoutInflater.from(parent.context).inflate(R.layout.comments_list_item, parent, false)
        return MyViewHolder(inflatingVariable)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindingFunction(commentsArrayList[position])
    }

    override fun getItemCount(): Int {
        return commentsArrayList.size
    }
}