package com.githubrepo.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.githubrepo.data.repository.GithubRepository

@Suppress("UNCHECKED_CAST")
class RepositoryViewModelFactory(
    private val repository: GithubRepository,
    private val forcefully: Boolean
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RepositoryViewModel(repository, forcefully) as T
    }
}