package com.bachhuberdesign.gwentcardviewer.inject.module

import android.app.Application
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.bachhuberdesign.gwentcardviewer.database.DatabaseOpenHelper
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import dagger.Module
import dagger.Provides
import rx.schedulers.Schedulers
import javax.inject.Singleton


/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@Module
class DatabaseModule(val application: Application) {

    @Provides
    @Singleton
    fun provideOpenHelper(): SQLiteOpenHelper {
        return DatabaseOpenHelper(application)
    }

    @Provides
    @Singleton
    fun provideSqlBrite(): SqlBrite {
        return SqlBrite.Builder()
                .logger { message -> Log.v("SQLBrite Database", message) }
                .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(sqlBrite: SqlBrite, helper: SQLiteOpenHelper): BriteDatabase {
        val db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io())
        db.setLoggingEnabled(true)
        return db
    }

}