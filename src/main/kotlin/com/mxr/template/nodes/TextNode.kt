package com.mxr.template.nodes

import com.mxr.template.Context

class TextNode(text: String) : Node(text) {

    override fun render(context: Context): String {
        return this.text
    }

    override fun toString(): String = "TextNode ${this.text}"

}