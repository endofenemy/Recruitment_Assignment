package com.githubrepo.ui.repository

import androidx.lifecycle.ViewModel
import com.githubrepo.data.repository.GithubRepository
import com.githubrepo.utils.lazyDeferred

class RepositoryViewModel(
    repository: GithubRepository
) : ViewModel() {

    val repository by lazyDeferred {
        repository.getRepository()
    }
}