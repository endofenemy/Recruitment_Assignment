package com.githubrepo.ui.repository

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.githubrepo.R
import com.githubrepo.data.db.entities.Repository
import com.githubrepo.databinding.ItemRepoListBinding
import java.util.*


private var currentPosition: Int? = null
private lateinit var context: Context

class RepositoryAdapter(
    private val repositoryList: List<Repository>
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryHolder>() {

    inner class RepositoryHolder(
        val binding: ItemRepoListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder {
        context = parent.context
        return RepositoryHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_repo_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = repositoryList.size

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        holder.binding.repository = repositoryList[position]

        if (currentPosition != null && currentPosition == position) {
            val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            holder.binding.childLayout.visibility = View.VISIBLE
            holder.binding.childLayout.startAnimation(slideDown)
        } else {
            holder.binding.childLayout.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            if (currentPosition != position) {
                val temp = currentPosition
                currentPosition = position
                temp?.let {
                    notifyItemChanged(it)
                }
                notifyItemChanged(position)
            } else {
                currentPosition = null
                notifyItemChanged(position)
            }
        }
    }

    fun sortByStars() {
        Collections.sort(
            repositoryList
        ) { t, t2 -> t2.stars!!.compareTo(t.stars!!) }

        notifyDataSetChanged()
    }

    fun sortByName() {
        Collections.sort(
            repositoryList
        ) { t, t2 -> t.author!!.compareTo(t2.author!!) }
        notifyDataSetChanged()
    }
}