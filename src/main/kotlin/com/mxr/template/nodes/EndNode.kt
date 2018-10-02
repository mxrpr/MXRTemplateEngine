package com.mxr.template.nodes

import com.mxr.template.Context

internal class EndNode(text: String): Node(text) {
    override fun render(context: Context): String = ""

    override fun toString(): String = "EndNode: '${this.text}'"
}