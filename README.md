# gwt-jet
The gwt-jet library provides a fast, flexible and easy way to wrap business objects that you want to show at the front-end. The jet classes automatically create the corresponding widget and automagically populate the user modified values into the original object.

Our first alpha release is here: 1.0.9.0 1.0.9.1 - check it at Downloads. (Project availability at Maven Central is coming soon)

#Almost a hello world example
Having a basic bean...

```java
public class MyBean implements Reflection, Serializable {
        
    private Date someDay;
    private String someText;
    private Integer someInt;
    private boolean someBoolean; (...)
```
    
...create a basic JetTable like this

```java
JetTable<MyBean> jetTable = new JetSingleTable<MyBean>();
jetTable.addColumn("someDay", "Some Day", 80, ReadOnlyCondition.NEVER);
jetTable.addColumn("someText", "Some Text", 170, ReadOnlyCondition.NEVER);
jetTable.addColumn("someInt", "Some Integer", 60, ReadOnlyCondition.NEVER);
jetTable.addColumn("someBoolean", "Some boolean", 60, ReadOnlyCondition.NEVER);
jetTable.setValues(myBeanCollection);
```

And you end up having something like this!

![alt text](https://github.com/neokyol/gwt-jet/blob/wiki/basicjettable.png "Image example 1")


When the user modifies any value, the corresponding values in your beans will be modified as well. Remember this occurs client side. If you obtained your myBeanCollection from the server, you could easily resend it back after the user confirms the changes.

#But is this flexible?
Very flexible. Wrappers for most common java types are automatically used (like the ones in the example), but you can create your owns or even redefine inline any to react as you want. The same with the read-only conditions. And [JetTable](https://github.com/neokyol/gwt-jet/blob/master/javadoc/ar/com/kyol/jet/client/JetTable.html) is a FlexTable so you can format it as usual. The key here is: make it fast, or make it as complex as you like.

Check more answers in the [FAQ](https://github.com/neokyol/gwt-jet/blob/wiki/FAQ.md) and more examples in the [Showcase](https://github.com/neokyol/gwt-jet/blob/wiki/Showcase.md) project

Credits
Most magic behind gwt-jet is provided by James Luo's wonderful project GWT ENT. The original source is included, but check its site if you want to use reflection some other way in your gwt project.
