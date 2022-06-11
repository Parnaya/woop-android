package com.example.woopchat.studying.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlin.coroutines.EmptyCoroutineContext

private suspend fun <T : Any, T2, R> FlowOperators() {
    val flow = flow<T> {}

    /** fusion */
    //Buffers flow emissions via channel of a specified capacity and runs collector in a separate coroutine.
    flow.buffer(capacity = BUFFERED, onBufferOverflow = BufferOverflow.SUSPEND)

    //The effect of this is that emitter is never suspended due to a slow collector, but collector always gets the most recent value emitted.
    flow.conflate()

    //Changes the context, where that flow is executed. If dispatchers is different, creates channel.
    flow.flowOn(EmptyCoroutineContext)

    //Returns a flow which checks cancellation status on each emission and throws the corresponding cancellation cause if flow collector was cancelled
    flow.cancellable()


    /** combine */
    //Transform flow by combining the most recently emitted values by each flow.
    flow.combine(flow = flowOf<T2>(), transform = { a: T, b: T2 -> })

    //Transform the most recently emitted values by each flow. Need emit to collector.
    flow.combineTransform(flow = flowOf<T2>(), transform = { a: T, b: T2 -> val collector: FlowCollector<R> = this })

    //Zip flows, using provided transform function
    flow.zip(other = flowOf(), transform = { a: T, b: T2 -> a == b })


    /** filter */
    //Returns flow where all subsequent repetitions of the same value are filtered out
    flow.distinctUntilChanged()
    flow.distinctUntilChangedBy(keySelector = { })

    //Returns a flow containing only values of the original flow that match the given predicate
    flow.filter(predicate = { false })
    flow.filterNot(predicate = { false })
    flow.filterNotNull()
    flow.filterIsInstance<Int>()


    /** errors */
    //Catches exceptions in the flow completion
    flow.catch(action = { cause: Throwable -> })

    //Retries flow up to retries times when an exception that matches the given predicate occurs in the upstream flow
    //Flow<T>.retryWhen(
    flow.retry()
    flow.retryWhen(predicate = { e: Throwable, attempt: Long -> true })


    /** collect */
    //Terminal flow operator that collects the given flow with a provided action.
    flow.collect(collector = { value: T -> })
    flow.collectIndexed(action = { index, value: T -> })

    //When the original flow emits a new value then the action block for the previous value is cancelled.
    flow.collectLatest(action = { value: T -> })

    //Terminal operator, that launches collection of the flow in the [scope]
    flow.launchIn(scope = CoroutineScope(EmptyCoroutineContext))

    //Terminal operator. Returns the number of elements in this flow
    flow.count(predicate = { true })

    //The terminal operator that returns the first element emitted by the flo
    flow.first()
    flow.first(predicate = { true })
    flow.firstOrNull()
    flow.firstOrNull(predicate = { true })

    //Terminate operator, that returns last value emitted by the flow
    flow.last()
    flow.lastOrNull()

    //The terminal operator that awaits for one and only one value to be emitted
    flow.single()
    flow.singleOrNull()

    //Terminal operators. Collects given flow into a destination
    flow.toCollection(ArrayList())
    flow.toSet()
    flow.toList()


    /** transform */
    //Returns flow, that maps emitted values with the [transform] fun
    flow.map(transform = { "$it" })
    flow.mapLatest(transform = { "$it" })
    flow.mapNotNull(transform = { "$it" })

    //The receiver of the transform is FlowCollector. It may transform emitted element, skip it or emit it multiple times.
    flow.transform(transform = { value: T -> emit("") })
    flow.transformLatest(transform = { value: T -> emit("") })
    flow.transformWhile(transform = { value: T -> emit(""); true })

    //maps flow value to Flow and collect its sequentially
    flow.map(transform = { flowOf<Int>() }).flattenConcat()
    flow.flatMapConcat(transform = { flowOf<Int>() })

    //maps flow value to Flow and collect its in parallel with [concurrency] parallel coroutines
    flow.map(transform = { flowOf<Int>() }).flattenMerge(concurrency = 8)
    flow.flatMapMerge(transform = { flowOf<Int>() })

    //When the original flow emits a new value, the previous transform block is cancelled.
    flow.flatMapLatest(transform = { flowOf<Int>() })
    flow.transformLatest(transform = { emit("$it") })

    //Wraps each value with IndexedValue
    flow.withIndex()


    /** take */
    //Returns flow that ignores some count of elements
    flow.drop(5)
    flow.dropWhile(predicate = { false })

    //Emit first count elements
    flow.take(count = 7)
    flow.takeWhile(predicate = { true })


    /** callback */
    //Invokes [action] after flow completed or canceled
    flow.onCompletion(action = { cause: Throwable? -> })

    //Invokes [action] before each value of the upstream flow is emitted downstream
    flow.onEach(action = { })

    //Invokes [action] when this flow completes without emitted values
    flow.onEmpty(action = { })

    //Invokes [action] before this flow starts to be collected
    flow.onStart(action = { })

    //Invokes [action] after this shared flow starts to be collected
    MutableSharedFlow<Unit>().onSubscription(action = { emit(Unit) })


    /** hotFlow */
    //Creates SharedFlow. Creates coroutine on [scope].
    flow.shareIn(scope = CoroutineScope(EmptyCoroutineContext), started = SharingStarted.Eagerly, replay = 0)

    //Creates StateFlow. Creates coroutine on [scope].
    flow.stateIn(scope = CoroutineScope(EmptyCoroutineContext), started = SharingStarted.Eagerly, initialValue = 1)

    //Collects flow in coroutine launched in scope from parameter and send results to channel
    lateinit var ch: ReceiveChannel<T>
    ch = flow.produceIn(scope = CoroutineScope(EmptyCoroutineContext))

    //Represents channel as flow, and consumes the channel. Can be consumed once.
    ch.consumeAsFlow()
    //Represents channel as flow, and receives from channel in fan-out fashion
    ch.receiveAsFlow()


    /** fold */
    //Terminal operator. Accumulates value starting with initial value and applying operation on each element
    flow.fold(initial = "", operation = { acc: String, value: T -> acc + "$value" })
    flow.reduce(operation = { acc: Any, value: T -> value }) //same as fold operator, but as initial value takes first element

    //Folds the given value with operation, emitting initial and every intermediate value
    flow.runningFold(initial = "", operation = { acc: String, value: T -> acc + "$value" })
    flow.runningReduce(operation = { acc: Any, value: T -> value })
    flow.scan(initial = "", operation = { acc: String, value: T -> acc + "$value" })


    /** time */
    //Filters out values that are followed by the newer values within the given timeout
    flow.debounce(timeoutMillis = 200)

    //Returns a flow that emits only the latest value emitted by the original flow during the given sampling period.
    flow.sample(100)
}
