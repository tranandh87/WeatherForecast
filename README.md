Supports API 16 and above

Run and build instruction:

Use Gradle to build the project. Android studio build in Gradle support could be handy.

Diving into implementation:

I have used two AsyncTaskLoader (one for current weather and other for weather forecast) in-order to avoid network call on configuration changes.
I have used RecyclerView, Adapter and ViewHolder for showing weather forecast result. Current we have only shown 3 days weather forecast but using RecylerView, Adapter and ViewHolder makes this app scalable with less code changes.
Implemented both local and instrumentation Unit test by extending TestCase and ActivityInstrumentationTestCase2 respectively.

Requirement to run this app:

This app uses "Fused Location Service", a location service in Google Play services to detect your current location. This means that only devices with the Play Store app installed and up to date will be able to use this application. You can also run on emulator with play services, locations and mock location data enabled. You can find how to enabled Google Play Services and location data at the end of this page. So, following are required to run this app in your device. 1. Phone with Play Store App installed 2. Phone with GPS enabled 3. Phone with Internet service enabled

Features:

Shows current location weather on launching the app
Show weather forecast for next 3 days
Can search with city name around the globe
Current location weather is provided by clicking the compass icon in the menu bar
This app shows only current location weather in landscape mode. You need to switch to portrait mode to see weather forecast and search for weather in other cities

Play services and location testing on emulators:

If you are using an AVD emulator, you must first make sure that your emulator images are up to date. To do that, open up your SDK Manager (Tools → Android → SDK Manager). Go down to the version of Android you plan to use for your emulator and ensure that the Google APIs System Images are both installed and up to date. If an update is available, click the button to install the update and wait until it is ready to go before continuing. Your AVD emulator also needs to have a target OS version that supports the Google APIs. When you create an emulator you can identify these target OS versions because they will say “Google APIs” on the right. Choose one with an API level of 21 or higher, and you will be all set. If you are going to use an emulator, it is recommend to use built-in AVD emulator over a Genymotion emulator. It is possible to use either, but setting up the Genymotion emulator for this purpose is not straighforward. Explore the documentation on Genymotion's website for more information.

Mock location data:

On an emulator you will also need some dummy (or mock) location updates to work with. Android Studio provides an Emulator Control panel that lets you send location points to the emulator. This works great on the old location services, but does nothing on the new Fused Location Provider. Instead, you have to publish mock locations programmatically.