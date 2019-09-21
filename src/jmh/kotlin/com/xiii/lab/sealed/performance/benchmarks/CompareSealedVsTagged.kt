package com.xiii.lab.sealed.performance.benchmarks

import com.xiii.lab.sealed.performance.*
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.lang.Exception

@BenchmarkMode(Mode.All)
@Warmup(iterations = 10)
@Measurement(iterations = 100, batchSize = 10)
open class CompareSealedVsTagged {

    @State(Scope.Benchmark)
    companion object TestData {
        private val exception = Exception("Test exception")
        private const val data = "Test data"

        private val taggedData = arrayOf(
                StateTagged(StateTagged.Type.LOADING),
                StateTagged(StateTagged.Type.DATA, data),
                StateTagged(StateTagged.Type.EMPTY),
                StateTagged(StateTagged.Type.ERROR, error = exception)
        )

        private val sealedData = arrayOf(
                Loading,
                Data(data),
                Empty,
                Error(exception)
        )
    }

    @Benchmark
    fun tagged(blackhole: Blackhole) {
        taggedData.map(::handleStateTagged).map(blackhole::consume)
    }

    @Benchmark
    fun sealed(blackhole: Blackhole) {
        sealedData.map(::handleStateSealed).map(blackhole::consume)
    }

    private fun handleStateTagged(state: StateTagged): Any = when (state.type) {
        StateTagged.Type.LOADING -> onLoading()
        StateTagged.Type.ERROR -> state.error?.run(::onError)
                ?: throw AssertionError("Unexpected error state: $state")
        StateTagged.Type.EMPTY -> onEmpty()
        StateTagged.Type.DATA -> state.data?.run(::onData)
                ?: throw AssertionError("Unexpected data state: $state")
    }

    private fun handleStateSealed(state: StateSealed): Any = when (state) {
        Loading -> onLoading()
        is Error -> onError(state.error)
        Empty -> onEmpty()
        is Data -> onData(state.data)
    }

    private fun onLoading() = Unit
    private fun onError(error: Throwable) = error
    private fun onEmpty() = Unit
    private fun onData(data: String) = data
}