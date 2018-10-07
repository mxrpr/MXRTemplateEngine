import com.mxr.template.Context
import com.mxr.template.MXRTemplateEngine
import com.mxr.template.nodes.*
import com.sun.org.apache.xpath.internal.ExpressionNode
import org.junit.Assert
import org.junit.Test

class TemplateTests {

    private fun runTemplate(templateString: String, context: Context): String {
        val templateEngine = MXRTemplateEngine(templateString)
        return templateEngine.parse(context)
    }

    @Test
    fun testVariable() {
        val context = Context()
        context.addVariable("testVariable", "Test content")
        val templateString = "this is a template with variable {{testVariable}}."
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "this is a template with variable Test content.")
    }

    @Test(expected = Exception::class)
    fun testVariable_not_found() {
        val context = Context()
        context.addVariable("tetVariable", "Test content")
        val templateString = "this is a template with variable {{testVariable}}."
        this.runTemplate(templateString, context)
    }

    @Test
    fun testTextNode() {
        val context = Context()
        val templateString = "this is a template without variable."
        val result = this.runTemplate(templateString, context)
        Assert.assertEquals(result, "this is a template without variable.")
    }

    @Test
    fun testCommentNode() {
        val context = Context()
        context.addVariable("testVariable", "Test content")
        val templateString = "a {{! this is a comment }} b"
        val result = this.runTemplate(templateString, context)
        Assert.assertEquals(result, "a  b")
    }

    // If the person key exists and has a non-false value, the content between the pound
    // * and slash will be rendered and displayed times.
    @Test
    fun testSectionNode_var_does_not_exists() {
        val context = Context()
        val templateString = "a {{#person}} should not appear \n{{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  b")
    }

    @Test
    fun testSectionNode_var_does_exists() {
        val context = Context()
        context.addVariable("person", true)
        val templateString = "a {{#person}} should  appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  should  appear  b")
    }

    @Test
    fun testSectionNode_var_does_exists_false() {
        val context = Context()
        context.addVariable("person", false)
        val templateString = "a {{#person}} should  not appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  b")
    }

    @Test
    fun testSectionNode_var_does_exists_empty_list() {
        val context = Context()
        context.addVariable("person", emptyArray<String>())
        val templateString = "a {{#person}} should  not appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  b")
    }


    // Value will be rendered if the key doesn't exist, is false, or is an empty list.
    @Test
    fun testInvertedSectionNode_key_does_not_exists() {
        val context = Context()
        val templateString = "a {{^person}} should  appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  should  appear  b")
    }

    // Value will be rendered if the key doesn't exist, is false, or is an empty list.
    @Test
    fun testInvertedSectionNode_key_does__exists_false() {
        val context = Context()
        context.addVariable("person", false)
        val templateString = "a {{^person}} should  appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  should  appear  b")
    }

    // Value will be rendered if the key doesn't exist, is false, or is an empty list.
    @Test
    fun testInvertedSectionNode_key_does__exists_true() {
        val context = Context()
        context.addVariable("person", true)
        val templateString = "a {{^person}} should not appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  b")
    }

    // Value will be rendered if the key doesn't exist, is false, or is an empty list.
    @Test
    fun testInvertedSectionNode_key_does__exists_emptyArray() {
        val context = Context()
        context.addVariable("person", emptyArray<String>())
        val templateString = "a {{^person}} should  appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  should  appear  b")
    }

    // Value will be rendered if the key doesn't exist, is false, or is an empty list.
    @Test
    fun testInvertedSectionNode_key_does__exists_not_emptyArray() {
        val context = Context()
        context.addVariable("person", arrayOf("1"))
        val templateString = "a {{^person}} should  appear {{/person}} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a  b")
    }

    @Test
    fun testEachNode() {
        val context = Context()
        context.addVariable("items", arrayOf("1"))
        val templateString = "a {% each items %}  {{it}} {% end %} b"
        val result = this.runTemplate(templateString, context)
        println("Result is: $result")
        Assert.assertEquals(result, "a   1  b")
    }

    @Test(expected = Exception :: class)
    fun testEachNode_no_variable() {
        val context = Context()
        val templateString = "a {% each items %}  {{it}} {% end %} b"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception :: class)
    fun testEachNode_variable_is_not_array() {
        val context = Context()
        context.addVariable("items", 2)
        val templateString = "a {% each items %}  {{it}} {% end %} b"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception :: class)
    fun testForNode_no_variable() {
        val context = Context()
        val templateString = "a {% for items2 in items %} {{= items2}} {% end %} b"
        this.runTemplate(templateString, context)
        Assert.fail("An exception should be thrown")
    }

    @Test
    fun testForNode() {
        val context = Context()
        context.addVariable("items", arrayOf("1", "2"))
        val templateString = "a {% for element in items %} {{element}} {% end %} b"
        val result = this.runTemplate(templateString, context)
        Assert.assertEquals(result, "a  1  2  b")
    }

    @Test(expected = Exception::class)
    fun testForNode_wrong_parsing() {
        val context = Context()
        context.addVariable("items", arrayOf("1", "2"))
        val templateString = "a {%forelementinitems %} {{element}} {% end %} b"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception::class)
    fun testForNode_variable_not_array() {
        val context = Context()
        context.addVariable("items", "not")
        val templateString = "a {% for element in items %} {{element}} {% end %} b"
        this.runTemplate(templateString, context)
    }

    @Test
    fun testIfNode_true() {
        val context = Context()
        context.addVariable("var", 2)
        context.addVariable("var2", 1)

        val templateString = "a {% if var > var2 %}  this should appear {% end %}"
        val result = this.runTemplate(templateString, context)
        println("Result: $result")
        Assert.assertEquals(result, "a   this should appear ")
    }

    @Test
    fun testIfNode_true_with_else() {
        val context = Context()
        context.addVariable("var", 2)
        context.addVariable("var2", 1)

        val templateString = "a {% if var > var2 %}  this should appear {% else %}This should not appear {% end %}"
        val result = this.runTemplate(templateString, context)
        println("Result: $result")
        Assert.assertEquals(result, "a   this should appear ")
    }

    @Test
    fun testIfNode_false() {
        val context = Context()
        context.addVariable("var", 2)
        context.addVariable("var2", 1)

        val templateString = "a {% if var < var2 %}  this should not appear {% end %}b"
        val result = this.runTemplate(templateString, context)
        println("Result: $result")
        Assert.assertEquals(result, "a b")
    }

    @Test(expected = Exception::class)
    fun testIfNode_parse_error() {
        val context = Context()
        context.addVariable("var", 2)
        context.addVariable("var2", 1)

        val templateString = "a {% ifvar<var2 %} this should not appear {% else %} this should appear b"
        this.runTemplate(templateString, context)
    }

//    @Test(expected = Exception::class)
//    fun testIfNode_without_end() {
//        val context = Context()
//        context.addVariable("var", 2)
//        context.addVariable("var2", 1)
//
//        val templateString = "a {% if var < var2 %} this should not appear {% else %} this should appear b"
//        val result = this.runTemplate(templateString, context)
//
//        Assert.assertEquals(result, "a  this should appear b")
//    }

    @Test
    fun testIfNode_with_else() {
        val context = Context()
        context.addVariable("var", 9)
        context.addVariable("var2", 1)

        val templateString = "a {% if var < var2 %} this should not appear {%else%} this should appear b {%end%}"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "a  this should appear b ")
    }

    @Test
    fun testIfNode_with_else2() {
        val context = Context()
        context.addVariable("var", 9)
        context.addVariable("var2", 1)

        val templateString = "a {% if var <= var2 %} this should not appear {%else%} this should appear b {%end%}"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "a  this should appear b ")
    }

    @Test
    fun testIfNode_with_else3() {
        val context = Context()
        context.addVariable("var", 9)
        context.addVariable("var2", 1)

        val templateString = "a {% if var >= var2 %} this should appear {%else%} this should not appear b {%end%}"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "a  this should appear ")
    }

    @Test(expected = Exception::class)
    fun testIfNode_variable_is_not_number_rhs() {
        val context = Context()
        context.addVariable("var", 9)
        context.addVariable("var2", "variable")

        val templateString = "a {% if var >= var2 %} this should appear {%else%} this should not appear b {%end%}"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception::class)
    fun testIfNode_variable_is_not_number_lhs() {
        val context = Context()
        context.addVariable("var", "variable1")
        context.addVariable("var2", "variable")

        val templateString = "a {% if var >= var2 %} this should appear {%else%} this should not appear b {%end%}"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception::class)
    fun testIfNode_with_no_variable_rhs() {
        val context = Context()
        context.addVariable("var", 9)
        val templateString = "a {% if var < var2 %} this should not appear {%else%} this should appear b {%end%}"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception::class)
    fun testIfNode_with_no_variable_lhs() {
        val context = Context()
        context.addVariable("var2", 9)
        val templateString = "a {% if var < var2 %} this should not appear {%else%} this should appear b {%end%}"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception::class)
    fun testIfNode_with_unknown_operation() {
        val context = Context()
        context.addVariable("var2", 9)
        context.addVariable("var", 9)
        val templateString = "a {% if var == var2 %} this should not appear {%else%} this should appear b {%end%}"
        this.runTemplate(templateString, context)
    }


    @Test
    fun testEmptyString() {
        val context = Context()
        val templateString = ""
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "")
    }

    @Test
    fun testString_without_any_template() {
        val context = Context()
        val templateString = "this is a test sentence"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "this is a test sentence")
    }

    @Test
    fun testString_with_spec_characters() {
        val context = Context()
        val templateString = "this is a {test sentence"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "this is a {test sentence")
    }

    @Test
    fun testString_with_expression() {
        val context = Context()
        context.addVariable("var1", 2)
        context.addVariable("var2", 2)
        val templateString = "this is a test sentence {{= var1 + var2 }}"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals(result, "this is a test sentence 4.0")
    }

    @Test
    fun testContext() {
        val context = Context()
        context.addVariable("var1", 2)

        Assert.assertTrue(context.containsVariable("var1"))
        Assert.assertEquals(context.getVariable("var1"), 2)

        context.clean()
        Assert.assertFalse(context.containsVariable("var1"))
    }

    @Test
    fun testExpression_with_wrong_content(){
        val context = Context()
        val templateString = "this is a test sentence {{= var1 + var2"
        val result = this.runTemplate(templateString, context)

        Assert.assertEquals("this is a test sentence {{= var1 + var2", result)
    }

    @Test
    fun testStringRepresentation(){
        Assert.assertEquals(VariableNode("{{var}}").toString(), "VariableNode: var")
        Assert.assertEquals(ElseNode("").toString(), "ElseNode")
        Assert.assertEquals(EachNode("{% each items %}").toString(), "EachNode: items")
        Assert.assertEquals(IfNode("{% if var < var2 %}").toString(), "IfNode: '{% if var < var2 %}'")
        Assert.assertEquals(SectionNode("{{#person}}").toString(), "SectionNode: '{{#person}}', variable: 'person'")
        Assert.assertEquals(InvertedSectionNode("{{^person}}").toString(), "InvertedSectionNode: '{{^person}}', variable: 'person'")
        Assert.assertEquals(TextNode("text").toString(), "TextNode text")
        Assert.assertEquals(CommentNode("{{!this is a comment}}").toString(), "CommentNode: {{!this is a comment}}")
        Assert.assertEquals(ExpressionNode("{{= {{var1}} + {{var2}} * ( {{var2}} / {{var1}}) }}").toString(),
                "ExpressionNode: {{var1}} + {{var2}} * ( {{var2}} / {{var1}})")
        Assert.assertEquals(RootNode().toString(), "RootNode: child number: 0")


    }
}