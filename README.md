# mis-2017-exercise-3-sensors-context

# Exercise 03 - Sensers & context

This app has two activities:
 -  First activity for:
Visualizing Live accelerometer sensor data in every axis, each axis is represented in a designated colored bar,  FFT-transformed data in a second graph.
- Second activity for music player for joggin & cycling.

# For the graphs:
We used the [Canvas](https://developer.android.com/reference/android/graphics/Canvas.html) library for the accelerometer graph, and [SparkView](https://github.com/robinhood/spark) for FFT-transformed graph.

# For the music player:
We used [Location.getSpeed()](https://developer.android.com/reference/android/location/Location.html) to get the speed of user movements.The thresholds are for jogging (1.9 Km/h to 10) and cycling (10 Km/h to 20).

We used the [MediaPlayer](https://developer.android.com/reference/android/media/MediaPlayer.html).

 