package com.githubrepo.ui.repository

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.githubissue.data.db.AppDatabase
import com.githubrepo.R
import com.githubrepo.data.network.MyApi
import com.githubrepo.data.network.NetworkConnectionInterceptor
import com.githubrepo.data.repository.GithubRepository
import com.githubrepo.utils.Coroutines

class RepositoryActivity : AppCompatActivity() {

    private lateinit var factory: RepositoryViewModelFactory
    private lateinit var repository: GithubRepository
    private lateinit var db: AppDatabase
    private lateinit var api: MyApi
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor

    private lateinit var viewModel: RepositoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        db = AppDatabase.invoke(this)
        api = MyApi(networkConnectionInterceptor)
        repository = GithubRepository(api, db)
        factory = RepositoryViewModelFactory(repository)

        viewModel = ViewModelProviders.of(this, factory).get(RepositoryViewModel::class.java)

        Coroutines.main {
            val repository = viewModel.repository.await()
            repository.observe(this, Observer {
                Toast.makeText(this, it.size.toString(), Toast.LENGTH_LONG).show()
            })
        }
    }
}
