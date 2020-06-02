# [Experimental] Discovery One

[ ![download](https://api.bintray.com/packages/danielebartorilla/DiscoveryOne/com.github.danielebart/images/download.svg) ](https://bintray.com/danielebartorilla/DiscoveryOne/com.github.danielebart/_latestVersion)
[![codecov](https://codecov.io/gh/danielebart/discovery-one/branch/master/graph/badge.svg)](https://codecov.io/gh/danielebart/discovery-one)
[![build](https://github.com/danielebart/discovery-one/workflows/master/badge.svg?branch=master)](https://github.com/danielebart/discovery-one/actions?query=workflow%3Amaster)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![License MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT)

Navigation in Android has always been a pain. This library abstracts the Android navigation allowing to easily create framework-agnostic Routes through an annotation processor and to navigate across your app without using a `Context` (with some caveats of course :D).

*This library is just an experiment and uses the new androidx activity and fragment libraries for registering results... don't use it in production!*

## Setup
#### Jcenter

Check that you have the `jcenter` repository in you `build.gradle`:

```
repositories {
    jcenter() // add it if needed
}
```

#### Add dependenices
There are two dependencies to add:
- `discoveryone-runtime` contains all the library code you need to navigate to a route.
- `discoveryone-compiler` must be applied using `kapt`, this will help you to generate you're routes automatically.


```
apply plugin: 'kotlin-kapt' // add the kotlin-kapt plugin to the top of your module build.gradle

def discoveryOneVersion = "0.11.0"

dependencies {
    ...
    implementation "com.github.danielebart:discoveryone-runtime:$discoveryOneVersion"
    ...
}

kapt "com.github.danielebart:discoveryone-compiler:$discoveryOneVersion"
```

## Creating a Route
A Route is an Android-independent simple object which may contains a list of argument.
To define a route you need to apply the relative route annotation to an `Activity`, `Fragment` or `DialogFragment`.
For instance, the code below will automatically generate a `MyFavouriteRoute` data class that you can create passing the two argument you defined in the `@FragmentRoute` annotation.

```
@FragmentRoute(
    name = "MyFavouriteRoute",
    containerId = R.id.container,
    arguments = [
        RouteArgument("my_argument_1", String::class),
        RouteArgument("my_argument_2", Int::class)
    ]
)
class MyFragment : Fragment()
```

## Navigating
`Navigator` is the only interface you need to navigate through routes.
In order to retrieve a `Navigator` instance you must use one of the extension functions available for you `Activity`, `Fragment` or `DialogFragment`.

```
class MyFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myNavigator = this.navigator // use the com.discoveryone.extensions.navigator extensions to retrieve a Navigator instance
    }
    

  ...
}
```

Once obtained a `Navigator` instance you can easily navigate to your routes using one of the navigate methods.

```
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            navigator.navigate(MyFavouriteRoute(my_argument_1 = "hello!", my_argument_2 = 1234))
        }
    }
}

```

This has the pleasant effect that you can handle your navigation logic outside of the Activity/Fragment context (see below for more info, be aware that if used wrongly it may produce unwanted leaks). For instance, you could navigate to a new Route in a `Presenter` or in your use cases without worrying about the Android Context.
**Important, since the internal implementation stores a reference to the activity you MUST NOT pass the Navigator instance to a ViewModel or to any object which lives longer then the Fragment/Activity**:
For instance:

```
class MyPresenter(private val navigator: Navigator, private val view: MyView) {

    fun onStart() {
        ...
    }

    fun onMyButtonClick() {
        view.someUICommand()
        navigator.navigate(MyFavouriteRoute(my_argument_1 = "hello!", my_argument_2 = 1234))
    }
    
    fun onCloseClick() {
        navigator.close()
    }
}
```
Then, you can easily test you presenter injecting a fake navigator or a mock.

**Please, be aware that any navigation operation MUST be executed in one of the android component lifecycle, this means that you can navigate after creating an Activity or after a Fragment has been attached to a Context.**

### Obtaining Route arguments in a safe way
This library generates for an extension proprties over an Android component for each one of the argument declared in the route annotation.
For instance, given an `Activity` route declared in the following way:
```
package com.myapp

@ActivityRoute( // name of the route is optional, if you don't set the name, the library is going to append to the activity name a postfix "Destination"
    arguments = [RouteArgument("helloArg", String::class)]
)
class MyActivity : AppCompatActivity {
    ...
}
```

the following property is going to be generated:
```
val MyActivity.helloArg: String
```

** as for the navigation operations, obtaining the arguments is an operation which involves the Android component lifecycle, be sure to use it in a lifecycle callback**


### Navigating for result
In particular situation you may need to return a result from the current page. This library supports the navigation for result to and from any kind of route (`Activity`, `Fragment` or `DialogFragment`).

First, you need to register for a result from the caller route:
```
navigator.onResult<Double, MyRoute> { result ->
    // do something with the result
}
```

Then, you just need to call the `Navigator` with the `navigateForResult` method:
```
class MyPresenter(private val navigator: Navigator, private val view: MyView) {

    fun onStart() {
        navigator.onResult<String, MyFavouriteRoute> { myResult ->
            view.updateMyLabel("returned result $myResult!")
        }
    }

    fun onMyButtonClick() {
        view.someUICommand()
        navigator.navigateForResult(MyFavouriteRoute(my_argument_1 = "hello!", my_argument_2 = 1234))
    }
}
```

Finally, to close a current route with a result:

```
@FragmentRoute(
    name = "MyFavouriteRoute",
    containerId = R.id.container,
    arguments = [
        RouteArgument("my_argument_1", String::class),
        RouteArgument("my_argument_2", Int::class)
    ]
)
class MyFragment : Fragment() {
    
    override onViewCreated(savedInstanceState: Bundle) {
        myButton.setOnClickListener {
        navigator.closeWithResult("here my result!")
  }
}
```

## License
```
MIT License

Copyright (c) 2020 Daniele Bartorilla

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to 
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN 
NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
