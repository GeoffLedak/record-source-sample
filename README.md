# record-source-sample

A compiled APK of this app to install on an Android device can be downloaded here:
http://geoffledak.com/recordsource/record-source-sample.apk

A slideout navigation drawer can be opened with the icon in the upper left.

The app's initial screen displays a scrollable list of record stores. Additional info about a store can be viewed by tapping on an item in the list. Tapping the store's phone number will kick off an intent to the devices phone dialer.

The maps section displays a map of Boulder with pins for each record store object that has latitude and longitude coordinates included with it. Tapping on a pin will display the name of the store.

The charts section has absolutely nothing to do with record stores, but it demonstrates custom drawables. If a number is entered into each text field and the 'draw' button is tapped, the bar chart underneath will be updated.

A network request is performed when the app is launched which populates the record store model. The JSON file that is downloaded is located at:
http://geoffledak.com/recordsource/stores.json

### Screenshots

![Store list](/screenshots/store-list.png "Store list")

![Store info](/screenshots/store-info.png "Store info")