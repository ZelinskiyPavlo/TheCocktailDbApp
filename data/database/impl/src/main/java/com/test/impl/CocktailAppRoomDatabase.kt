package com.test.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.test.database.model.UserDbModel
import com.test.database.model.cocktail.*
import com.test.impl.dao.CocktailDao
import com.test.impl.dao.UserDao
import com.test.impl.typeconverter.DateConverter
import com.test.impl.typeconverter.StringListToStringConverter

@Database(
    version = 2,
    entities = [CocktailEntity::class, NameEntity::class, InstructionEntity::class,
        IngredientEntity::class, CocktailIngredientJunction::class,
        CocktailMeasureJunction::class, MeasureEntity::class, UserDbModel::class],
    views = [CocktailDbModel::class],
    exportSchema = false
)
@TypeConverters(DateConverter::class, StringListToStringConverter::class)
abstract class CocktailAppRoomDatabase : RoomDatabase() {

    abstract fun cocktailDao(): CocktailDao
    abstract fun userDao(): UserDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """ALTER TABLE cocktail_entity 
                    ADD COLUMN date_added INTEGER NOT NULL 
                    CONSTRAINT default_date DEFAULT (0)"""
                )
                database.execSQL("DROP VIEW CocktailDbModel")
                database.execSQL(
                    """CREATE VIEW `CocktailDbModel` AS SELECT cocktail_entity.id AS id, 
name_entity.name_default AS name_defaults, name_entity.name_alternate AS name_default_alternate, 
name_entity.name_es AS name_es, name_entity.name_es AS name_de, name_entity.name_fr AS name_fr, 
name_entity.name_zn_hans AS name_zn_hans, name_entity.name_zn_hant AS name_zn_hant, 
cocktail_entity.category AS category, cocktail_entity.alcohol_type as alcoholType, 
cocktail_entity.glass AS glass, cocktail_entity.image AS image, 
instruction_entity.instruction_default AS instruction_defaults, 
instruction_entity.instruction_alternate AS instruction_default_alternate, 
instruction_entity.instruction_es AS instruction_es, 
instruction_entity.instruction_es AS instruction_de, 
instruction_entity.instruction_fr AS instruction_fr, 
instruction_entity.instruction_zn_hans AS instruction_zn_hans, 
instruction_entity.instruction_zn_hant AS instruction_zn_hant, 
ingredients, 
measures, 
cocktail_entity.is_favorite AS isFavorite, cocktail_entity.date_added AS dateAdded
FROM cocktail_entity 
INNER JOIN (
SELECT cocktail_entity.id, GROUP_CONCAT(ingredient_entity.ingredient) AS ingredients
FROM cocktail_entity 
INNER JOIN cocktail_ingredient_junction ON cocktail_entity.id = cocktail_ingredient_junction.cocktail_id
INNER JOIN ingredient_entity ON cocktail_ingredient_junction.ingredient = ingredient_entity.ingredient
GROUP BY cocktail_entity.id
) AS ingredient_join ON cocktail_entity.id = ingredient_join.id
INNER JOIN (
SELECT cocktail_entity.id, GROUP_CONCAT(measure_entity.measure) AS measures
FROM cocktail_entity 
INNER JOIN cocktail_measure_junction ON cocktail_entity.id = cocktail_measure_junction.cocktail_id
INNER JOIN measure_entity ON cocktail_measure_junction.measure = measure_entity.measure
GROUP BY cocktail_entity.id
) AS measure_join ON cocktail_entity.id = measure_join.id
INNER JOIN name_entity ON cocktail_entity.id = name_entity.cocktail_id 
INNER JOIN instruction_entity ON cocktail_entity.id = instruction_entity.cocktail_id 
WHERE name_entity.cocktail_id = cocktail_entity.id AND instruction_entity.cocktail_id = cocktail_entity.id 
GROUP BY cocktail_entity.id"""
                )
            }
        }
    }
}