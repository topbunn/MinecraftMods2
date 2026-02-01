package com.l13devstudio.domain.useCases.file

import com.l13devstudio.domain.repository.FileRepository

class FileExistsUseCase(
    private val repository: FileRepository
) {

    suspend operator fun invoke(fileName: String) = repository.exists(fileName)
}