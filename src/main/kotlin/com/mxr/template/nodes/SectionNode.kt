package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

/**
 * Sections render blocks of text one or more times, depending on the value of the
 * key.A section begins with a pound and ends with a slash. That is, {{#person}}
 * begins a "person" section while {{/person}} ends it.

 * The behavior of the section is determined by the value of the key.

 * If the person key exists and has a value of false or an empty list, the content
 * between the pound and slash will not be displayed.
 * If the person key exists and has a non-false value, the content between the pound
 * and slash will be rendered and displayed times.
*/
internal class SectionNode(text: String): Node(text) {
    var variableName: String = text.substring(3, this.text.length-2)

    init {
        this.hasScope = true
    }

    override fun render(context: Context): String {
        // if the variable does not exists, then return with empty string
        if (!context.containsVariable(this.variableName))
            return ""

        val result = StringBuilder(500)
        val variableValue = context.getVariable(this.variableName)

        if (variableValue is Boolean && variableValue == true ||
                variableValue is Array<*> && variableValue.size > 0) {
            for (child in this.childrens) {
                result.append(child.render(context))
            }
        }

        return result.toString()
    }

    override fun toString(): String = "SectionNode '${this.text}', variable: '${this.variableName}'"
}