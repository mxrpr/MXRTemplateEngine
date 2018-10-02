package com.mxr.template.nodes

import com.mxr.template.Context

/**
 * Comment node
 *
 * Example:
 * {{! this is a comment }}
 */
internal class CommentNode(text: String) : Node(text) {

    override fun render(context: Context): String = ""

    override fun toString(): String = "CommentNode: ${this.text}"
}