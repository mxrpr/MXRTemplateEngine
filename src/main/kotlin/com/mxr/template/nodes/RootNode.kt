package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

/**
 * This is the root node. All parsed node objects will be
 * childs of this node.
 * During content generation, this  object is called.
 */
internal class RootNode: Node() {


    override fun render(context: Context) : String {
        val result = StringBuilder(1000)
        for (child in this.childrens) {
            result.append(child.render(context))
        }
        return result.toString()
    }

    override fun toString(): String = "RootNode, child number: ${this.childrens.size}"

}