### What it is?

This application redirects URL requests from another already-compiled application

### Requirements:

Java 8 only

### Usage:

1. Edit URLMocker.urls file
   Structure:
   odd number of lines
   - MOCKED_URL_SUBSTRING
   - NEW_DESTINATION
   - ANOTHER_URL
   - DESTINATION
   See [URLMocker.urls](URLMocker.urls) as example

2. Place URLMocker.urls in working directory of target application
3. Run target as:
```sh
java -javaagent:URLMocker.jar -DurlMocker.doRedirect=true -cp classes_of_target_application main.class.of.target
```