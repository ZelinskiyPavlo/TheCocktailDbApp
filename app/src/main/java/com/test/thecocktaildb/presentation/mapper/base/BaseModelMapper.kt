package com.test.thecocktaildb.presentation.mapper.base

abstract class BaseModelMapper<OutModel : Any, InModel : Any> {

    open fun mapTo(model: InModel): OutModel =
        throw NotImplementedError("provide mapping for model ${model!!::class.java.simpleName}")

    open fun mapFrom(model: OutModel): InModel =
        throw NotImplementedError("provide mapping for model ${model!!::class.java.simpleName}")

    fun mapToList(model: List<InModel>): List<OutModel> = model.map { mapTo(it) }
    fun mapFromList(model: List<OutModel>): List<InModel> = model.map { mapFrom(it) }
}