/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amphibians.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import kotlinx.coroutines.launch

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    init {
        Log.d("AmphibianApiStatus", "initializing api...")
        getAmphibianList()
        Log.d("AmphibianApiStatus", "Success: initializing api")
    }

    // Create properties to represent MutableLiveData and LiveData
    private val _status = MutableLiveData<AmphibianApiStatus> ()
    val status = _status
    private var _amphibianList = MutableLiveData<List<Amphibian>> ()
    val amphibianList = _amphibianList
    private var _selectedAmphibian = MutableLiveData<Amphibian> ()
    val selectedAmphibian = _selectedAmphibian

    // Get a list of amphibians from the api service and set the status via a Coroutine
    private fun getAmphibianList() {
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try {
                _amphibianList.value = AmphibianApi.retrofitService.getAmphibians()
                _status.value = AmphibianApiStatus.DONE
            } catch (e: Exception) {
                Log.e("AmphibianViewModel", e.stackTraceToString())
                _status.value = AmphibianApiStatus.ERROR
                _amphibianList.value = listOf()
            }
        }
    }

    // Set the amphibian object
    fun onAmphibianClicked(amphibian: Amphibian) {
        _selectedAmphibian.value = amphibian
    }
}
