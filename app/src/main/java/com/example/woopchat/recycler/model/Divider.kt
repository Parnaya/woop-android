package com.example.woopchat.recycler.model

import com.example.woopchat.recycler.diff.Diffable

data class Divider(
    override val id: Any,
    val size: Float,
    val colored: Boolean = false,
    val marginTop: Float = 0f,
    val marginLeft: Float = 0f,
    val marginRight: Float = 0f,
    val marginBottom: Float = 0f,
) : Diffable