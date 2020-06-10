import 'dart:io';

import 'package:flutter/services.dart' show rootBundle;
import 'package:flutter/material.dart';
import 'package:idealmealv1/activity_screens/aboutus.dart';
import 'package:idealmealv1/activity_screens/restaurants.dart';
import 'package:csv/csv.dart' as csv;

List<String> restaurants = [];
List<String> restaurantMenuItems = [];
List<List<dynamic>> csv_variable;

void main() {
  loadAsset();
  for (int i=0; i<csv_variable.length; i++) {
    if(csv_variable[i][2].compareTo('Restaurant') != 0 || csv_variable[i][2].length != 0) {
      if (restaurants.length != 0) {
        for (int j = 0; j < restaurants.length; j++) {
          if (csv_variable[i][2].compareTo(restaurants[j]) != 0) {
            restaurants.add(csv_variable[i][2]);
          }
        }
      } else {
        restaurants.add(csv_variable[i][2]);
      }
    }
  }
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'IdealMeal',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.lightGreen,
        // This makes the visual density adapt to the platform that you run
        // the app on. For desktop platforms, the controls will be smaller and
        // closer together (more dense) than on mobile platforms.
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: new _MyHomePageState(),
    );
  }
}


class _MyHomePageState extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text('IdealMeal'),
      ),
      drawer: Drawer(
        child: ListView(
        children: <Widget>[
          UserAccountsDrawerHeader(
            accountName: Text("IdealMeal"),
            accountEmail: Text("idealmeal.com"),
            currentAccountPicture: CircleAvatar(
          backgroundColor:
          Theme.of(context).platform == TargetPlatform.iOS
              ? Colors.lightGreenAccent
              : Colors.white,
              child: Text(
            "A",
            style: TextStyle(fontSize: 40.0),
          ),
        ),
      ),
          ListTile(
            title: Text("Home"),
            trailing: Icon(Icons.arrow_forward),
         onTap: () {
              Navigator.push(context, MaterialPageRoute(builder: (context) => _MyHomePageState()));
         }),
          ListTile(
            title: Text("Restaurants"),
            trailing: Icon(Icons.arrow_forward),
          onTap: () {
              Navigator.push(context, MaterialPageRoute(builder: (context) => RestaurantPage()));
          }),
          ListTile(
            title: Text("About Us"),
            trailing: Icon(Icons.arrow_forward),
        onTap: () {
              Navigator.push(context, MaterialPageRoute(builder: (context) => AboutUsPage()));
        })],
        ),
      ),
      body:
      CustomMultiChildLayout(
        delegate: MyMultiChildDelegate(position: Offset.zero),
        children: [
          LayoutId(
            id: 1,
            child: Text('LOOKING FOR SOMETHING TO EAT?')
          ),
          LayoutId(
            id: 2,
            child: Text('Discover a menu catered for you')
          ),
          LayoutId(
            id: 3,
            child: Text('or')
          ),
          LayoutId(
            id: 4,
            child: Text('Visit us at IdealMeal.biz')
          ),
          LayoutId(
            id: 5,
            child: FlatButton(
              onPressed: () {},
              child: Text('Go!')
        ))]
      ),
    );
  }
}

List<List<dynamic>> csvToList(File myCSVFile) {
  csv.CsvToListConverter c =
      new csv.CsvToListConverter(eol: "\r\n", fieldDelimiter: ",", shouldParseNumbers: true);
  List<List> listCreated = c.convert(myCSVFile.readAsStringSync());
  return listCreated;
}

loadAsset() async {
  final myData = await rootBundle.loadString('/Users/user/AndroidStudioProjects/MP-Spring2020-JacobDanielLee/ideal_meal_v1/WorkingDocIdealMeal.csv');
  csv_variable = csv.CsvToListConverter().convert(myData);

}


class MyMultiChildDelegate extends MultiChildLayoutDelegate {
  MyMultiChildDelegate({this.position});

  final Offset position;
  
  @override
  void performLayout(Size size) {
    Size leadingSize = Size.zero;
    Size secondSize = Size.zero;
    Size thirdSize = Size.zero;
    Size fourthSize = Size.zero;
    Size fifthSize = Size.zero;

    if (hasChild(1)) {
      leadingSize = layoutChild(1,
          BoxConstraints(maxWidth: size.width, maxHeight: size.height));
    }
    if (hasChild(2)) {
      secondSize = layoutChild(2,
          BoxConstraints(maxWidth: size.width, maxHeight: size.height));
    }
    if (hasChild(3)) {
      thirdSize = layoutChild(3,
          BoxConstraints(maxWidth: size.width, maxHeight: size.height));
    }
    if (hasChild(4)) {
      fourthSize = layoutChild(4,
          BoxConstraints(maxWidth: size.width, maxHeight: size.height));
    }
    if (hasChild(5)) {
      fifthSize = layoutChild(5,
          BoxConstraints(maxWidth: size.width, maxHeight: size.height));
    }

    positionChild(1,
      Offset(
        size.width / 2 - leadingSize.width / 2, // This will place child 2 to the right of child 1.
        size.height / 2 - 110, // Centers the second child vertically.
      ),
    );

    positionChild(2,
      Offset(
        size.width / 2 - secondSize.width / 2, // This will place child 2 to the right of child 1.
        size.height / 2 - 75, // Centers the second child vertically.
      ),
    );

    positionChild(3,
      Offset(
        size.width / 2 - thirdSize.width / 2, // This will place child 2 to the right of child 1.
        size.height / 2 - 50, // Centers the second child vertically.
      ),
    );

    positionChild(4,
      Offset(
        size.width / 2 - fourthSize.width / 2, // This will place child 2 to the right of child 1.
        size.height / 2 - 25, // Centers the second child vertically.
      ),
    );

    positionChild(5,
      Offset(
        size.width / 2 - fifthSize.width / 2, // This will place child 2 to the right of child 1.
        size.height / 2, // Centers the second child vertically.
      ),
    );

  }

  bool shouldRelayout(MyMultiChildDelegate oldDelegate) {
    return oldDelegate.position != position;
  }

}
