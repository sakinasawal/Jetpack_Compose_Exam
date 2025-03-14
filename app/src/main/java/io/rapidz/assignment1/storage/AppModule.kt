package io.rapidz.assignment1.storage

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.rapidz.assignment1.dao.AppDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreInterface {
		return DataStoreManager(context)
	}

	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
		return Room.databaseBuilder(
			context,
			AppDatabase::class.java,
			"app_database"
		).build()
	}

	@Provides
	@Singleton
	fun provideAppDao(db: AppDatabase) = db.appDao()
}
