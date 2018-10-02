package com.mxr.template.nodes

/**
 * Comment node
 * Example:
 * {{! this is a comment }}
 */
class CommentNode(text: String) : Node(text) {

    override fun toString(): String = "CommentNode: ${this.text}"
}