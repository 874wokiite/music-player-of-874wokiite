package com.example.music_player_of_874wokiite.features.musicDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.music_player_of_874wokiite.components.SampleData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SampleViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()

    private val _sampleList = MutableLiveData<List<SampleData>>()
    val sampleList: LiveData<List<SampleData>> = _sampleList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchSampleList() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("SampleList").get().await()
                val sampleDataList = snapshot.documents.map { document ->
                    document.toObject(SampleData::class.java) ?: SampleData()
                }
                _sampleList.postValue(sampleDataList)
                _errorMessage.postValue(null)
            } catch (e: Exception) {
                Log.e("SampleViewModel", "Error fetching sample list", e)
                _errorMessage.postValue(e.message)
            }
        }
    }
}