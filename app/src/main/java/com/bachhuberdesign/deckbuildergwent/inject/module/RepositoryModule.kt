package com.bachhuberdesign.deckbuildergwent.inject.module

import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardRepository
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.google.gson.Gson
import com.squareup.sqlbrite.BriteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@Module(includes = arrayOf(DatabaseModule::class))
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDeckRepository(database: BriteDatabase): DeckRepository {
        return DeckRepository(database)
    }

    @Provides
    @Singleton
    fun provideCardRepository(database: BriteDatabase): CardRepository {
        return CardRepository(database)
    }

}