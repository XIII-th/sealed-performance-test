package com.xiii.lab.sealed.performance

/**
 * @author XIII-th
 * @since 14.09.2019
 */
class StateTagged(
        val type: Type,
        val data: String? = null,
        val error: Throwable? = null
) {
    enum class Type { LOADING, ERROR, EMPTY, DATA }
}