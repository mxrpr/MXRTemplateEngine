package com.mxr.template.nodes

import com.mxr.template.Context

internal abstract class Node() {

    val childrens = mutableListOf<Node>()
    var hasScope = false
    var text: String = ""

    constructor(text: String="") : this() {
        this.text = text
    }

    abstract  fun render(context: Context): String
}