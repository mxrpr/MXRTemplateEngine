package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

class ElseNode(text: String) : Node(text) {

    override fun render(context: Context): String {
        val result = StringBuilder(500)
        for (child in this.childrens) {
            result.append(child.render(context))
        }
        return result.toString()
    }

    override fun toString(): String = "ElseNode"
}