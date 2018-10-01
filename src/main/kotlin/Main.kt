import com.mxr.template.Context
import com.mxr.template.MXRTemplateEngine

fun main(args: Array<String>) {
    val templateEngine = MXRTemplateEngine("<html>{{var1}}</html>")
    val context = Context()
    context.addVariable("var1","Test string")

    templateEngine.parse(context)
}