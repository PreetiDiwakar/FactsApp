package com.wipro.facts.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wipro.facts.data.FactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FactsViewModel @Inject constructor(
    repository: FactsRepository
) : ViewModel() {
    val facts = repository.getFacts().asLiveData()
}