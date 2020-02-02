package com.githubrepo.data.network.response

import com.githubrepo.data.db.entities.Repository


data class RepositoryResponse(
    val repositories: List<Repository>
)