package com.hamit.domain.useCases.file

import com.hamit.domain.repository.FileRepository

class DeleteFileUseCase(
    private val repository: FileRepository
) {

    suspend operator fun invoke(fileName: String) = repository.deleteFile(fileName)
}