package com.bachhuberdesign.gwentcardviewer.inject.module

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckRepository
import com.google.gson.Gson
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
    fun provideDeckRepository(gson: Gson): DeckRepository {
        return DeckRepository(gson)
    }

}