import com.mxr.template.Context
import com.mxr.template.MXRTemplateEngine
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
        println("Result: '$result'")
        Assert.assertEquals(result, "this is a template with variable Test content.")
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
        Assert.fail("An exception should be thrown")
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
    fun testIfNode_false_with_else() {
        val context = Context()
        context.addVariable("var", 2)
        context.addVariable("var2", 1)

        val templateString = "a {% if var < var2 %} this should not appear {% else %} this should appear {% end %}b"
        this.runTemplate(templateString, context)
    }

    @Test(expected = Exception::class)
    fun testIfNode_without_end() {
        val context = Context()
        context.addVariable("var", 2)
        context.addVariable("var2", 1)

        val templateString = "a {% if var < var2 %} this should not appear {% else %} this should appear b"
        val result = this.runTemplate(templateString, context)
        println("Result: $result")
        Assert.assertEquals(result, "a  this should appear b")
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

}