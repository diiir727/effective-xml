# effective-xml
High level, memory efficient XML parser.
It's very usefully when you need to parse some array of objects from big xml. It based on javax.xml.stream.XMLStreamReader,
so it read file line by line and not use too much memory.

## Examples

So we have simple xml:
```xml
<breakfast_menu>
    <food>
        <name>Belgian Waffles</name>
        <price share="EUR">5.95</price>
        <description>Two of our famous Belgian Waffles with plenty of real maple syrup</description>
        <calories>650</calories>
    </food>
    <food>
        <name>Strawberry Belgian Waffles</name>
        <price share="USD">7.95</price>
        <description>Light Belgian waffles covered with strawberries and whipped cream</description>
        <calories>900</calories>
    </food>
    <food>
        <name>Berry-Berry Belgian Waffles</name>
        <price share="RUB">8.95</price>
        <description>Light Belgian waffles covered with an assortment of fresh berries and whipped cream</description>
        <calories>900</calories>
    </food>
</breakfast_menu>
```
Next lines of code parse all "food" elements to the list. The properties of tag will be in Map by name with "@", for example "share"
property you can find by "@share" key. The value of tag will be in Map with key "#val".

```java
        ListObserver observer = new ListObserver();
        StreamParser parser = new StreamParser(observer);

        try (var is = new FileInputStream(Utils.getFile("food-menu.xml"))){
            parser.parse(is, "food");
        }

        List<Map<String, Object>> results = observer.getResults();

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals("Belgian Waffles", ((Map<String, Object>)results.get(0).get("name")).get("#val"));
        Assertions.assertEquals("EUR", ((Map<String, Object>)results.get(0).get("price")).get("@share"));
```