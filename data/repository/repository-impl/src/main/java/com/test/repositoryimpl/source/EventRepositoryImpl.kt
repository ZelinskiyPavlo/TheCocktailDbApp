package com.test.repositoryimpl.source

import androidx.lifecycle.MutableLiveData
import com.test.common.Event
import com.test.repository.source.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(): EventRepository {

    override val noCocktailWithIdFoundEvent = MutableLiveData<Event<Unit>>()
}