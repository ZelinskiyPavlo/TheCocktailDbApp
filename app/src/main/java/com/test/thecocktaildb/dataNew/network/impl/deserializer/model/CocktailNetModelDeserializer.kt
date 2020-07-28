package com.test.thecocktaildb.dataNew.network.impl.deserializer.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.test.thecocktaildb.dataNew.network.impl.extension.getMemberStringOrEmpty
import com.test.thecocktaildb.dataNew.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.dataNew.network.model.cocktail.LocalizedStringNetModel
import java.lang.reflect.Type

class CocktailNetModelDeserializer : JsonDeserializer<CocktailNetModel> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ): CocktailNetModel {

        return json!!.asJsonObject!!.let { jsonObject ->
            val drinksJsonArray = jsonObject.get("drinks").asJsonArray
            val drinkJsonObject = drinksJsonArray.first().asJsonObject

            CocktailNetModel(
                id = drinkJsonObject.get("id").asLong,
                names = LocalizedStringNetModel(
                    drinkJsonObject.getMemberStringOrEmpty("strDrink"),
                    drinkJsonObject.getMemberStringOrEmpty("strDrinkAlternate"),
                    drinkJsonObject.getMemberStringOrEmpty("strDrinkES"),
                    drinkJsonObject.getMemberStringOrEmpty("strDrinkDE"),
                    drinkJsonObject.getMemberStringOrEmpty("strDrinkFR"),
                    drinkJsonObject.getMemberStringOrEmpty("strDrinkZH-HANS"),
                    drinkJsonObject.getMemberStringOrEmpty("strDrinkZH-HANT")
                ),
                category = drinkJsonObject.getMemberStringOrEmpty("strCategory"),
                alcoholType = drinkJsonObject.getMemberStringOrEmpty("strAlcoholic"),
                glass = drinkJsonObject.getMemberStringOrEmpty("strGlass"),
                image = drinkJsonObject.getMemberStringOrEmpty("strDrinkThumb"),
                instructions = LocalizedStringNetModel(
                    default = drinkJsonObject.getMemberStringOrEmpty("strInstructions"),
                    es = drinkJsonObject.getMemberStringOrEmpty("strInstructionsES"),
                    de = drinkJsonObject.getMemberStringOrEmpty("strInstructionsDE"),
                    fr = drinkJsonObject.getMemberStringOrEmpty("strInstructionsFR"),
                    zhHans = drinkJsonObject.getMemberStringOrEmpty("strInstructionsZH-HANS"),
                    zhHant = drinkJsonObject.getMemberStringOrEmpty("strInstructionsZH-HANT"),
                ),
                ingredients = mutableListOf<String>().apply {
                    repeat(15) { i ->
                        drinkJsonObject.getMemberStringOrEmpty("strIngredient${i + 1}")
                    }
                }.toList(),
                measures = mutableListOf<String>().apply {
                    repeat(15) { i ->
                        drinkJsonObject.getMemberStringOrEmpty("strMeasure${i + 1}")
                    }
                }.toList()
            )
        }
    }
}
//                Це напевно таким способом потрібно викликати якраз цей десеріалайзер
//                list.add(context.deserialize(it, deserializeType<CocktailNetModel>()))


//EXAMPLE
//    @Throws(JsonParseException::class)
//    override fun deserialize(json: JsonElement?, type: Type, context: JsonDeserializationContext): ClinicNetModel {
//        return json!!.asJsonObject!!.let { jsonObject ->
//            val checkMetroKey = if (jsonObject.has("metro_id")) "metro_id" else "metro_ids"
//            val metroIds = jsonObject.getIfHasMember(checkMetroKey).let { element ->
//                when {
//                    element == null -> emptyList()
//                    element.isJsonArray -> element.asJsonArray.map { if (it.isJsonPrimitive) it.asLong else -1L }
//                        .filter { it > -1L }
//                    element.isJsonPrimitive -> listOf(element.asLong)
//                    else -> emptyList()
//                }
//            }
//            val checkMetroStationsKey = if (jsonObject.has("metro_station")) "metro_station" else "metro_stations"
//            val metroStations = jsonObject.getIfHasMember(checkMetroStationsKey).let { element ->
//                when {
//                    element == null -> emptyList()
//                    element.isJsonArray -> element.asJsonArray.map { it.asStringOrEmpty() }.filter { it.isNotEmpty() }
//                    else -> listOf(element.asStringOrEmpty()).filter { it.isNotEmpty() }
//                }
//            }
//
//            val id = jsonObject.get("id").asLong
//            ClinicNetModel(
//                id = id,
//                cityId = jsonObject.get("city_id").asLong,
//                name = jsonObject.getMemberStringOrEmpty("name"),
//                image = jsonObject.getMemberStringOrEmpty("image"),
//                address = jsonObject.getMemberStringOrEmpty("address"),
//                districtId = jsonObject.getIfHasMember("district_id")?.asLong ?: -1L,
//                neighbourhood = jsonObject.getMemberStringOrEmpty("neighbourhood"),
//                metroIds = metroIds,
//                metroStations = metroStations,
//                latitude = jsonObject.get("location_lat").asDouble,
//                longitude = jsonObject.get("location_lon").asDouble,
//                rating = jsonObject.getIfHasMember("rating")?.asInt ?: 0,
//                specialties = jsonObject.getIfHasMember("specialties")
//                    ?.let {
//                        context.deserialize<List<ClinicDoctorSpecialtyNetModel>>(it, clinicDoctorSpecialtyListType)
//                            .apply { forEach { it.clinicId = id } }
//                    }
//                    ?: emptyList()
//            )
//        }
//    }