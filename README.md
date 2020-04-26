# Discovery One

[![codecov](https://codecov.io/gh/danielebart/discovery-one/branch/master/graph/badge.svg)](https://codecov.io/gh/danielebart/discovery-one)
[![build](https://github.com/danielebart/discovery-one/workflows/master/badge.svg?branch=master)](https://github.com/danielebart/discovery-one/actions?query=workflow%3Amaster)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.3.72-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![License MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT)

Navigation in Android has always been a pain. This library simplify how two destination interact with each other in order to easily navigate without using the Context.

## Setup
[WIP]

## Navigating to a Destination
Each activity or fragment must be annotated with `@ActivityNavigationDestination` or `@FragmentNavigationDestination`.
Once you did that, the library will generate for you a `Destination` class which can be easily passed as argument to the `Navigator` instance. For instance, given a simple Activity:
```
@ActivityNavigationDestination(
    name = "DESTINATION_X",
    arguments = [
      DestinationArgument("argNameX", String::class),
      DestinationArgument("argNameY", Int::class)
    ]
)
class MyActivity : FragmentActivity()
```
Then, you can easily navigate to that Activity from any point of your code without using a `Context` reference:
```
class MyPresenter {

    fun onButtonClick() {
        // no need for context!
        Navigator.navigate(DESTINATION_X(argNameX = "valueX", argNameY = 345))
    }
}
```

### Navigating for result [WIP]
This library also support the navigation for result. 

First of all, a result callback must be registered within the Navigator instance and must be placed where it can always be executed after a configuration change or process kill (for instance, during an Activity or Presenter instantiation, or in the `onCreate` method).

```
// registering an action
val resultToken = Navigator.registerResult<Double> { result ->
    // do something with the result!
}
```
`registerResult` returns a token that must be consumed by the `navigateForResult` method of the `Navigator` object, you can obviously have multiple result tokens for different navigation results.
```
// navigating for result using the above result token.
Navigator.navigateForResult<Double>(DESTINATION_X, resultToken)
```

## How does it work?

### Destination Generation
[WIP]

### Context interceptor
[WIP]

### Activity and Fragment Result
[WIP]

## Features
- [x] Simple navigation between Fragments and Activities.
- [x] Navigation for result between Activities.
- [ ] Navigation for result between Fragments. *TODO*.
- [ ] Support back navigation. *WIP*.
- [ ] Dialogs and BottomSheets destinations. *TODO*.
- [ ] Support transactions. *TODO*.
- [ ] Improve Jvm testing API. *TODO*.

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
