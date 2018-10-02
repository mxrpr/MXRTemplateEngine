package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

internal class ElseNode(text: String) : Node(text) {

    override fun render(context: Context): String {
        val result = StringBuilder(500)
        var endFound = false
        for (child in this.childrens) {
            if (child is EndNode) {
                endFound = true
                break
            }
            result.append(child.render(context))
        }

        if (!endFound)
            throw Exception("No end tag defined for ElseNode: '${this.text}'")

        return result.toString()
    }

    override fun toString(): String = "ElseNode"
}