import 'package:flutter/material.dart';
import 'package:idealmealv1/activity_screens/aboutus.dart';
import 'package:idealmealv1/activity_screens/restaurants.dart';


class MyHomePage extends StatelessWidget {

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
                  Navigator.push(context, MaterialPageRoute(builder: (context) => MyHomePage()));
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
