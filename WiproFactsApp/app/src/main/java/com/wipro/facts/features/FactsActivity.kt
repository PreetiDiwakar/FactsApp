package com.wipro.facts.features

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.wipro.facts.databinding.ActivityFactsBinding
import com.wipro.facts.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FactsActivity : AppCompatActivity() {
    // Helps to preserve the view
    // If the app is closed, then after reopening it the app will open
    // in a state in which it was closed

    // DaggerHilt will inject the view-model for us
    private val viewModel: FactsViewModel by viewModels()

    val factsAdapter = FactsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The bellow segment would instantiate the activity_facts layout
        // and will create a property for different
        // views inside it!
        val binding = ActivityFactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI(binding)


        binding.swipeRefresh.setOnRefreshListener{
            fetchAllFacts(binding)
        }
    }

    fun fetchAllFacts(binding: ActivityFactsBinding) {

             binding.swipeRefresh.setRefreshing(true);
                viewModel.facts.observe(this@FactsActivity) { result ->
                factsAdapter.submitList(result.data)

                binding.progressBar.isVisible = result is Resource.Loading<*> && result.data.isNullOrEmpty()
                binding.textViewError.isVisible = result is Resource.Error<*> && result.data.isNullOrEmpty()
                binding.textViewError.text = result.error?.localizedMessage
                binding.swipeRefresh.setRefreshing(false);
            }
    }

    fun initUI(binding: ActivityFactsBinding) {
        binding.apply {
            recyclerViewer.apply {
                adapter = factsAdapter
                layoutManager = LinearLayoutManager(this@FactsActivity)
            }
        }
        fetchAllFacts(binding)
    }
}

