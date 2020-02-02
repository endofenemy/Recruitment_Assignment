package com.githubrepo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubissue.data.db.AppDatabase
import com.githubrepo.data.db.entities.Repository
import com.githubrepo.data.network.MyApi
import com.githubrepo.data.network.SafeApiRequest
import com.githubrepo.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {
    private val repositories = MutableLiveData<List<Repository>>()

    init {
        repositories.observeForever {
            saveRepository(it)
        }
    }


    suspend fun getRepository(): LiveData<List<Repository>> {
        return withContext(Dispatchers.IO) {
            fetchRepository()
            db.getRepositoryDao().getAllRepository()
        }
    }

    private suspend fun fetchRepository() {
        if (isFetchNeeded()) {
            val response = apiRequest { api.getRepository() }
            Log.e("Response", response.toString())
            repositories.postValue(response)
        }
    }

    private fun isFetchNeeded(): Boolean {
        return true
    }

    private fun saveRepository(repository: List<Repository>) {
        Coroutines.io {
            db.getRepositoryDao().saveAllRepository(repository)
        }
    }
}