package com.githubrepo.ui.repository

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubissue.data.db.AppDatabase
import com.githubrepo.R
import com.githubrepo.data.network.MyApi
import com.githubrepo.data.network.NetworkConnectionInterceptor
import com.githubrepo.data.preferences.PreferenceProvider
import com.githubrepo.data.repository.GithubRepository
import com.githubrepo.utils.Coroutines
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.placeholder_layout.*

class RepositoryActivity : AppCompatActivity() {

    private lateinit var factory: RepositoryViewModelFactory
    private lateinit var repository: GithubRepository
    private lateinit var db: AppDatabase
    private lateinit var api: MyApi
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var preferenceProvider: PreferenceProvider

    private lateinit var viewModel: RepositoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceProvider = PreferenceProvider(this)
        networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        db = AppDatabase.invoke(this)
        api = MyApi(networkConnectionInterceptor)
        repository = GithubRepository(api, db, preferenceProvider)
        factory = RepositoryViewModelFactory(repository, false)

        viewModel = ViewModelProviders.of(this, factory).get(RepositoryViewModel::class.java)

        shimmer_view_container.startShimmerAnimation()

        Coroutines.main {
            val repository = viewModel.repository.await()
            repository.observe(this, Observer {
                shimmer_view_container.stopShimmerAnimation()
                shimmer_view_container.visibility = View.GONE

                if (it.isNotEmpty()) {
                    error_layout.visibility = View.GONE
                    // Setup Recycler view
                    recycler_view.layoutManager = LinearLayoutManager(this)
                    recycler_view.adapter = RepositoryAdapter(it)
                    recycler_view.visibility = View.VISIBLE
                } else {
                    error_layout.visibility = View.VISIBLE
                }
            })
        }

        retry.setOnClickListener {
            Coroutines.io {
                repository.fetchRepository(true)
            }
        }
    }

}
