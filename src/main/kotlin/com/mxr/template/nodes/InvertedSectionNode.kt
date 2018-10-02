package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

/**
 * An inverted section begins with a caret (hat) and ends with a slash.
 * That is {{^person}} begins a "person" inverted section while {{/person}} ends it.
 *
 * Value will be rendered if the key doesn't exist, is false, or is an empty list.
 */
class InvertedSectionNode(text: String) : Node(text) {
    var variableName: String = text.substring(3, this.text.length - 2)

    init {
        this.hasScope = true
    }

    override fun render(context: Context): String {

        val result = StringBuilder(500)
        val variableValue = context.getVariable(this.variableName)

        if (variableValue == null ||
                (variableValue is Boolean) && variableValue == false ||
                (variableValue is Array<*>) && variableValue.isEmpty()) {
            for (child in this.childrens) {
                result.append(child.render(context))
            }
        }

        return result.toString()
    }

    override fun toString(): String = "InvertedSectionNode '${this.text}', variable: '${this.variableName}'"
}