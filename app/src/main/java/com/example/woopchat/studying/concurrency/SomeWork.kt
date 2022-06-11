package com.example.woopchat.studying.concurrency

interface SomeWork {
    fun produce(workload: Runnable)
    fun consume(): Runnable
}