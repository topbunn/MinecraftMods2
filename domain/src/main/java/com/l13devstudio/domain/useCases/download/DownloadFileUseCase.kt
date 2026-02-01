package com.l13devstudio.domain.useCases.download

import com.l13devstudio.domain.repository.DownloadRepository

class DownloadFileUseCase(
    private val repository: DownloadRepository
) {

    operator fun invoke(url: String, filename: String) = repository.download(url = url, fileName = filename)

}