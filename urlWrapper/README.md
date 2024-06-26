### What it is?

This application redirects URL requests from another already-compiled application

### Requirements:

Java 8 only

### Usage:

1. Edit URLWrapper.urls file
   Structure:
   odd number of lines
   - MOCKED_URL_SUBSTRING
   - NEW_DESTINATION
   - ANOTHER_URL
   - DESTINATION
   See [URLWrapper.urls](URLWrapper.urls) as example

2. Place URLWrapper.urls in working directory of target application
3. Run target as:
```sh
java -javaagent:URLWrapper.jar -DurlWrapper.doRedirect=true -cp classes_of_target_application main.class.of.target
```