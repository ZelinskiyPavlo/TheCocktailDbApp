package com.test.repository.source

import androidx.lifecycle.MutableLiveData
import com.test.common.Event

/** I don't know how good this solution is. But I can't think about any better way to organize
 *  communication between features. Perhaps I need to store this "repository" in app module, so that
 *  features wouldn't know event about this interface. But I don't want to make app module too big.
 *  I also don't know which naming to choose if I will extract this class to app module. On the other
 *  hand I saw some mentions about creating in-memory repository. So, for now I will stick with
 *  this implementation. Maybe I need to use some sort of event Bus.
* */
interface EventRepository {

    // TODO: 21.02.2021 Rework using coroutines (after that remove unused dependencies)
    val noCocktailWithIdFoundEvent: MutableLiveData<Event<Unit>>
}