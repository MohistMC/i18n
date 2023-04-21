# i18n

### Damn modularity makes ```ResourceBundle.getBundle``` not work properly, so make a separate Jar


### USE

Gradle
```
 repositories {
    maven {url "https://maven.mohistmc.com/"}
 }

dependencies {
    implementation 'com.mohistmc:i18n:0.1'
}
```  

Code
```
// Init
i18n i18n = new i18n().build(ClassLoader classLoader, Locale locale);
// Use
i18n.get(key);
```

Resource
```
Create a new default file: \resources\lang\message.properties

More language files: \resources\lang\message_xx_XX.properties

```