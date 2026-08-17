package com.example.marginal.di

import com.example.marginal.data.repository.AuthRepositoryImpl
import com.example.marginal.data.repository.NoteRepositoryImpl
import com.example.marginal.domain.repository.AuthRepository
import com.example.marginal.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
}
