package com.despaircorp.ui.main.main_activity.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UiProvideModule {
    @Provides
    @Singleton
    fun provideRandomProfilePicturinator() = ProfilePictureRandomizator
    
    
}