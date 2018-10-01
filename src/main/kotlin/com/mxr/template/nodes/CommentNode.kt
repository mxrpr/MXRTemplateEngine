package com.mxr.template.nodes

import com.mxr.template.Context

class CommentNode(text: String) : Node(text) {

    override fun toString(): String = "CommentNode: ${this.text}"
}