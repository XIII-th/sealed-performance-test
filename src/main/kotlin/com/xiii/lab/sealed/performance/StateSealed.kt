package com.xiii.lab.sealed.performance

/**
 * @author XIII-th
 * @since 14.09.2019
 */
sealed class StateSealed

object Loading : StateSealed()
data class Error(val error: Throwable) : StateSealed()
object Empty : StateSealed()
data class Data(val data: String) : StateSealed()