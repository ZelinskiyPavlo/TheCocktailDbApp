package com.test.database.model.cocktail

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.test.database.Table
import java.util.*

@DatabaseView(
    """SELECT ${Table.COCKTAIL}.id AS id, 
${Table.NAME}.name_default AS name_defaults, ${Table.NAME}.name_alternate AS name_default_alternate, 
${Table.NAME}.name_es AS name_es, ${Table.NAME}.name_es AS name_de, ${Table.NAME}.name_fr AS name_fr, 
${Table.NAME}.name_zn_hans AS name_zn_hans, ${Table.NAME}.name_zn_hant AS name_zn_hant, 
${Table.COCKTAIL}.category AS category, ${Table.COCKTAIL}.alcohol_type as alcoholType, 
${Table.COCKTAIL}.glass AS glass, ${Table.COCKTAIL}.image AS image, 
${Table.INSTRUCTION}.instruction_default AS instruction_defaults, 
${Table.INSTRUCTION}.instruction_alternate AS instruction_default_alternate, 
${Table.INSTRUCTION}.instruction_es AS instruction_es, 
${Table.INSTRUCTION}.instruction_es AS instruction_de, 
${Table.INSTRUCTION}.instruction_fr AS instruction_fr, 
${Table.INSTRUCTION}.instruction_zn_hans AS instruction_zn_hans, 
${Table.INSTRUCTION}.instruction_zn_hant AS instruction_zn_hant, 
ingredients, 
measures, 
${Table.COCKTAIL}.is_favorite AS isFavorite, ${Table.COCKTAIL}.date_added AS dateAdded
FROM ${Table.COCKTAIL} 
INNER JOIN (
SELECT ${Table.COCKTAIL}.id, GROUP_CONCAT(${Table.INGREDIENT}.ingredient) AS ingredients
FROM ${Table.COCKTAIL} 
INNER JOIN ${Table.COCKTAIL_INGREDIENT} ON ${Table.COCKTAIL}.id = ${Table.COCKTAIL_INGREDIENT}.cocktail_id
INNER JOIN ${Table.INGREDIENT} ON ${Table.COCKTAIL_INGREDIENT}.ingredient = ${Table.INGREDIENT}.ingredient
GROUP BY ${Table.COCKTAIL}.id
) AS ingredient_join ON ${Table.COCKTAIL}.id = ingredient_join.id
INNER JOIN (
SELECT ${Table.COCKTAIL}.id, GROUP_CONCAT(${Table.MEASURE}.measure) AS measures
FROM ${Table.COCKTAIL} 
INNER JOIN ${Table.COCKTAIL_MEASURE} ON ${Table.COCKTAIL}.id = ${Table.COCKTAIL_MEASURE}.cocktail_id
INNER JOIN ${Table.MEASURE} ON ${Table.COCKTAIL_MEASURE}.measure = ${Table.MEASURE}.measure
GROUP BY ${Table.COCKTAIL}.id
) AS measure_join ON ${Table.COCKTAIL}.id = measure_join.id
INNER JOIN ${Table.NAME} ON ${Table.COCKTAIL}.id = ${Table.NAME}.cocktail_id 
INNER JOIN ${Table.INSTRUCTION} ON ${Table.COCKTAIL}.id = ${Table.INSTRUCTION}.cocktail_id 
WHERE ${Table.NAME}.cocktail_id = ${Table.COCKTAIL}.id AND ${Table.INSTRUCTION}.cocktail_id = ${Table.COCKTAIL}.id 
GROUP BY ${Table.COCKTAIL}.id"""
)
class CocktailDbModel(
    val id: Long = -1L,
    @Embedded(prefix = "name_")
    val names: LocalizedStringDbModel? = LocalizedStringDbModel(),
    val category: String? = "",
    val alcoholType: String? = "",
    val glass: String? = "",
    val image: String? = "",
    @Embedded(prefix = "instruction_")
    val instructions: LocalizedStringDbModel? = LocalizedStringDbModel(),
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList(),
    val isFavorite: Boolean? = false,
    val dateAdded: Date = Calendar.getInstance().time
)