# Life Overview

Modified from [cse.msu.edu assignment](http://www.cse.msu.edu/~cse231/PracticeOfComputingUsingPython/08_ClassDesign/Life/)

See also [wiki entry](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)

1. Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

# Requirements
Install gradle (i.e. `brew install gradle` or something specific to your platform)

Run wrapper: 
```
gradle wrapper
```

# Running
Requires Gradle
```
./gradlew run
```

# Testing
* TODO

# TODO
* Really should write some tests :-(
* Should make seed and number of children configurable.
* Would be nice to be able to run the generations until a stable pattern is found, if possible.
* Would be super cool to turn the output into an animated gif.
* Should test out some of the patterns in the wiki entry.
* Would be nice to have a random grid generator  
