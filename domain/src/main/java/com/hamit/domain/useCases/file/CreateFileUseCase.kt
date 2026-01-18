package com.hamit.domain.useCases.file

import com.hamit.domain.repository.FileRepository

class CreateFileUseCase(
    private val repository: FileRepository
) {

    suspend operator fun invoke(fileName: String) = repository.createFile(fileName)
}