# DataSynth



```
{
  "entities" : [
    {
      "name" : "person",
      "number" : 10000,
      "attributes" : [
        {
          "name" : "country",
          "type" : "String",
          "generator" : {
            "name" : "org.dama.datasynth.generators.CDFGenerator",
            "requires" : [],
            "init" : ["/dicLocations.txt",1,5," "]
          }
        },
        {
          "name" : "name",
          "type" : "String",
          "generator" : {
            "name" : "org.dama.datasynth.generators.CorrellationGenerator",
            "requires" : ["person.country"],
            "init" : ["/namesByCountry.txt"," "]
          }
        }
      ]
    }
  ]
}
```
