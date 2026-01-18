package com.hamit.domain.useCases.file

import com.hamit.domain.repository.FileRepository

class OpenFileUseCase(
    private val repository: FileRepository
) {

    suspend operator fun invoke(fileName: String) = repository.openFile(fileName)
}