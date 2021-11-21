package com.test.repositoryimpl.source

import com.test.repository.source.EventRepository
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(): EventRepository {

    override val noCocktailWithIdFoundChannel = Channel<Unit>(capacity = Channel.CONFLATED)
}