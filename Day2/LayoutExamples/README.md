Android Layouts
====
Although there are several layouts in android, this app will showcase only some common layouts such as
ConstraintLayout, LinearLayout (vertical, horizontal) and RelativeLayout. 

Introduction
---
Layout is used to define the user interface for an app or activity and it will hold the UI elements that will appear to the user. All elements in the layout are built using a hierarchy of View and ViewGroup objects.

![view_and_viewGroup](https://developer.android.com/images/viewgroup_2x.png)

##### Examples of ViewGroup - LinearLayout, RelativeLayout, ConstraintLayout, etc.
##### Examples of View - TextView, EditText, Button, etc.

ViewGroup Types
---
### Linear Layout
A view group that aligns all children in a single direction, vertically or horizontally.
 It creates a scrollbar if the length of the window exceeds the length of the screen. <br/>
![LinearLayout from tutorialspoint](https://www.tutorialspoint.com/android/images/liner.jpg)

### Relative Layout
 A view group that displays child views in relative positions. The position of each view can be specified as relative to sibling elements (such as to the left-of or below another view) or in positions relative to the parent RelativeLayout area (such as aligned to the bottom, left or center). <br/>
 ![RelativeLayout from tutorialspoint](https://www.tutorialspoint.com/android/images/relative.jpg)
 
### Constraint Layout
A view group where child views can be positioned in adaptable and flexible ways. It allows you to create large and complex layouts with a flat view hierarchy. <br/>
![constraintlayout example](https://abhiandroid.com/ui/wp-content/uploads/2018/07/Constraint-Layout-Example-In-Android-Studio.png)

### Table Layout
A layout that arranges its children into rows and columns. A TableLayout consists of a number of TableRow objects, each defining a row. TableLayout containers do not display border lines for their rows, columns, or cells. Each row has zero or more cells; each cell can hold one View object. The table has as many columns as the row with the most cells. A table can leave cells empty. Cells can span columns, as they can in HTML. <br/>
![TableLayout from tutorialspoint](https://www.tutorialspoint.com/android/images/table.jpg)

### Absolute Layout
A view group enables you to specify the exact location of its children. (This layout was deprecated in API level 3 and it's not suitable for production of software that's going to be on multiple devices as it gets tied to a particular screen size. In other words, this layout is not responsive). <br/>
![AbsoluteLayout from tutorialspoint](https://www.tutorialspoint.com/android/images/absolute.jpg)

### Frame Layout
The FrameLayout is a placeholder on screen that you can use to display a single view. Generally, FrameLayout should be used to hold a single child view, because it can be difficult to organize child views in a way that's scalable to different screen sizes without the children overlapping each other. Below is the example of FrameLayout. <br/>
![FrameLayout from tutorialspoint](https://www.tutorialspoint.com/android/images/frame.jpg)

### Motion Layout
A view group that manages view motion and widget animations. <br/>
![MotionLayout example](https://images.zoftino.com/development/android-dev/ui/motionlayout-keyframe-example.gif)

### Coordinator Layout
A view group that enables views to inherit the attributes of the underlying view. CoordinatorLayout is a super-powered FrameLayout. It is intended for two primary use cases:
- As a top-level application decor or chrome layout
- As a container for a specific interaction with one or more child views 

<br/>

![Coordinator Layout](https://miro.medium.com/max/758/1*fVKOTpH7S2ZlGrpmLcuyZQ.gif)

### List View
ListView is a view group that displays a list of scrollable items. <br/>
![list view from tutorialspoint](https://www.tutorialspoint.com/android/images/list.jpg)

### Grid View 
GridView is a ViewGroup that displays items in a two-dimensional, scrollable grid. <br/>
![grid view](https://lh3.googleusercontent.com/proxy/37uxE9NfeqQE2qmqV84Uxof4ohmom1L0y0x8D946uUa4RPRlJy9-Z3yPU2G3izxaOjwhMQATABMgV3ZSvZH33jbsR6lHd8JWT0zP78M)

### Adapter View
AdapterView is ViewGroup that displays items loaded into a adapter. It is a bridge between UI component and data source that helps us to fill data in UI component. It holds the data and send the data to an Adapter view then view can takes the data from the adapter view and shows the data on different views like as ListView, GridView, etc.


