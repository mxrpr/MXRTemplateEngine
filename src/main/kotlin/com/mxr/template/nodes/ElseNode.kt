package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

internal class ElseNode(text: String) : Node(text) {

    override fun render(context: Context): String  = ""

    override fun toString(): String = "ElseNode"
}