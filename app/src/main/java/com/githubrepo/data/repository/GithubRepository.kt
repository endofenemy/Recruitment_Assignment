package com.githubrepo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubissue.data.db.AppDatabase
import com.githubrepo.data.db.entities.Repository
import com.githubrepo.data.network.MyApi
import com.githubrepo.data.network.SafeApiRequest
import com.githubrepo.data.preferences.PreferenceProvider
import com.githubrepo.utils.Constants
import com.githubrepo.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val preferences: PreferenceProvider
) : SafeApiRequest() {
    private val repositories = MutableLiveData<List<Repository>>()

    init {
        repositories.observeForever {
            saveRepository(it)
        }
    }


    suspend fun getRepository(forcefully: Boolean): LiveData<List<Repository>> {
        return withContext(Dispatchers.IO) {
            fetchRepository(forcefully)
            db.getRepositoryDao().getAllRepository()
        }
    }

    suspend fun fetchRepository(forcefully: Boolean) {
        if (forcefully || isFetchNeeded(preferences.getLastSavedAt())) {
            try {
                val response = apiRequest { api.getRepository() }
                Log.e("Response", response.toString())
                repositories.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    // return true if Last Cache Time is more than 2 hours otherwise return false
    private fun isFetchNeeded(lastSavedAt: String?): Boolean {
        if (lastSavedAt == null)
            return true             // If Caching First time return true
        else {
            val lastSavedTime = lastSavedAt.toLong()
            val currentTime = System.currentTimeMillis()
            Log.e("Current Time $currentTime", " Saved Times $lastSavedTime")
            return ((currentTime - lastSavedTime) > Constants.DURATION)
        }
    }

    private fun saveRepository(repository: List<Repository>) {
        Coroutines.io {
            preferences.saveLastSavedAt(System.currentTimeMillis().toString())
            db.getRepositoryDao().saveAllRepository(repository)
        }
    }
}