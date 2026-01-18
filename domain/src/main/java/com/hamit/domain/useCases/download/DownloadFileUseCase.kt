package com.hamit.domain.useCases.download

import com.hamit.domain.repository.DownloadRepository

class DownloadFileUseCase(
    private val repository: DownloadRepository
) {

    operator fun invoke(url: String, filename: String) = repository.download(url = url, fileName = filename)

}