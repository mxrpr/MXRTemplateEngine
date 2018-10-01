package com.mxr.template.nodes

import com.mxr.template.Context

open class Node() {

    val childrens = mutableListOf<Node>()
    var hasScope = false
    var text: String = ""

    constructor(text: String) : this() {
        this.text = text
    }

    open fun render(context: Context): String = ""

    protected fun renderChilds() {}

    override fun toString(): String = "Node : ${this.text}"
}