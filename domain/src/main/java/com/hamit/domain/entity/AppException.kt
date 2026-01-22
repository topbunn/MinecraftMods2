package com.hamit.domain.entity

open class AppException: Exception()

object NoInternet: AppException()
object Maintenance: AppException()
object Error: AppException()

enum class AppExceptionType {
    NoInternet, Maintenance, Error
}
