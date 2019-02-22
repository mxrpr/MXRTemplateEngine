
**Simple template engine written in Kotlin, which provides a simple syntax similar to Mustache.**

**Features:**

**Variable:**
```kotlin
{{my_var}}
```

**Comment:** 
```kotlin
{{! this is a comment }}
```

**For cycle:** 
```kotlin
{% for items2 in items %}
	{{= items2}}
{% end %}
```

**If statement:** 
```kotlin
{% if number1 > number2 %}
 ...
{%else%}
	...
{%end%}
```

**Section:** </BR>
_A section begins with a pound and ends with a slash. That is, {{#my_var}}
begins a "my_var" section while {{/my_var}} ends it._

```kotlin
{{#my_var}}
  ...
{{/my_var}}
```

**Inverted section:** </br>
_An inverted section begins with a caret (hat) and ends with a slash.
That is {{^my_var}} begins a "my_var" inverted section while {{/my_var}} ends it.
Value will be rendered if the key doesn't exist, is false, or is an empty list._

```kotlin
{{^my_var}}
...
{{/my_var}}
```

**Each statement:** 
```kotlin
{% each items %}
  {{it}}
{% end %}
```

**Example**:
```text
This is a simple template
{! this is a comment}
This is a value:
<div>{{my_var}}</div> 

{% each items %}
    <div>{{it}}</div>
    {% for items2 in items %}
    	   {{items2}}
    	    {% for items3 in items %}
    	    	items {{items2}} / {{items3}}
    	    	{% if 4 > 3 %}
    	    		This is true
    	    	{% else %}
    	    		this is false
    	    	{% end %}
    	    {% end %}
    {% end %}
{% end %}
```
**Template engine usage**
```Kotlin
import com.mxr.template.Context
import com.mxr.template.MXRTemplateEngine

fun main(args: Array<String>) {
    val templateEngine = MXRTemplateEngine("<html>{{var1}}</html>")
    val context = Context()
    context.addVariable("var1","Test string")

    println(templateEngine.parse(context))
}
```
