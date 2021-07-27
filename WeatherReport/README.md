# WeatherReport

The WeatherReport is an app where the user can see the weather details of thier current location.
The app has features as follows
* The app fetches the current location and get the weather information for that location from http://openweathermap.org/API and show the details inside the app screen.
* The app has a background work manager which fetches the weather information once in every 15 mins(but the requirement is every 2 hours, but testing purpose I reduced it to 15 mins in order to avoid high wait time), The background work manager calls the openweathermap api only when the network is connected through wifi.
* The code stuctures following MVVM model and the api request are handled using Retrofit.
* The app is using the Room database to store the weather details on background.
* Please download the [apk](https://github.com/cstraji/WeatherReport/blob/main/weatherReport.apk) to preview the app and share the feedback.

