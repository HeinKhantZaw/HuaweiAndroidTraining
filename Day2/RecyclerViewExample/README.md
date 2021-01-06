RecyclerView
=================

An android app demo that showcases recycler view. RecyclerView is a subclass of ViewGroup and is a more resource-efficient way to display scrollable lists. 
Instead of creating a View for each item that may or may not be visible on the screen,
RecyclerView creates a limited number of list items and reuses them for visible content.
It is introduced in Marshmallow and it is an advanced version of the ListView with improved performance and other benefits.

Introduction:
---
This repo includes a customAdapter to show in recyclerView, itemList class containing text, icon and a main activity where we add our recyclerView.
First of all, we create a custom layout list.xml in layout folder.

list.xml
---
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/linearLayout"
    android:layout_width="match_parent"
    android:padding="8dp"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        app:srcCompat="@mipmap/ic_launcher_round" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="Item"
        android:textSize="24sp"
        android:textStyle="bold" />
</LinearLayout>

```

Then, we create ItemList.java class. This class is used as a POJO class which sets the properties of the items (for TextView and ImageView).

ItemList.java
----
```java
class ItemList {
    private String text;
    private int icon;

    public ItemList(String text, int icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }

}
```
Now, let's define the Adapter that associates the above data with the ViewHolder views.

CustomAdapter.java
---
```java
package com.example.recyclerviewexample;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ItemList[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            imageView = view.findViewById(R.id.imageView);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapter(ItemList[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        final ItemList list = localDataSet[position];
        viewHolder.textView.setText(list.getText());
        viewHolder.imageView.setImageResource(list.getIcon());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), list.getText() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
```
In this class, we have to implement an Adapter and ViewHolder.
These two classes work together to define how the data is displayed. 
The ViewHolder class is a wrapper around a View that contains the layout for an individual item in the list.
The CustomAdapter class creates ViewHolder objects as needed, and also sets the data for those views.

#### onCreateViewHolder()
RecyclerView calls this method whenever it needs to create a new ViewHolder. The method creates and initializes the ViewHolder and its associated View, but this method does not fill in the view's contents 
because the ViewHolder has not yet been bound to specific data. To show data, we have to override another method called ``onBindViewHolder()``.

#### onBindViewHolder() 
RecyclerView calls this method to associate a ViewHolder with data. 
The method fetches the appropriate data and uses the data to fill in the view holder's layout. 
As shown in the code above, ``setText()``, ``setImageResource()`` are called to fill the view holder's layout.
Then ``setOnClickListener()`` is added to make a toast when the user clicks on each item.

#### getItemCount()
RecyclerView calls this method to get the size of the data set.
In this code, it returns localDataSet.length so that RecyclerView can use this to determine when there are no more items that can be displayed.

MainActivity.java
---
In MainActivity.java, we first create a array of itemList to show in recyclerView.
```java
 ItemList[] itemList = initArray();
 
 private ItemList[] initArray() {
        return new ItemList[]{
                    new ItemList("help icon", android.R.drawable.ic_menu_help),
                    new ItemList("email icon", android.R.drawable.ic_dialog_email),
                    new ItemList("map icon", android.R.drawable.ic_dialog_map),
                    new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm),
                    new ItemList("lock icon", android.R.drawable.ic_lock_idle_lock),
                    new ItemList("search icon", android.R.drawable.ic_search_category_default),
                    new ItemList("zoom icon", android.R.drawable.ic_menu_zoom),
                    new ItemList("direction icon", android.R.drawable.ic_menu_directions),
                    new ItemList("lock icon", android.R.drawable.ic_lock_idle_lock),
                    new ItemList("search icon", android.R.drawable.ic_search_category_default),
                    new ItemList("zoom icon", android.R.drawable.ic_menu_zoom),
                    new ItemList("map icon", android.R.drawable.ic_dialog_map),
                    new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm),
                    new ItemList("direction icon", android.R.drawable.ic_menu_directions),
                    new ItemList("help icon", android.R.drawable.ic_menu_help),
                    new ItemList("email icon", android.R.drawable.ic_dialog_email),
                    new ItemList("map icon", android.R.drawable.ic_dialog_map),
                    new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm),
                    new ItemList("direction icon", android.R.drawable.ic_menu_directions)
            };
    }
```
Then, we create a CustomAdapter variable and pass the array we created (itemList) as an argument. 
```java
CustomAdapter adapter = new CustomAdapter(itemList);
```

After that, we have to create a recyclerView to show our data.

```java
  recyclerView = findViewById(R.id.recyclerView);
  recyclerView.setLayoutManager(new LinearLayoutManager(this));
  recyclerView.setAdapter(adapter);
```
``setLayoutManager()`` is used to position the individual items on the screen and determine when to reuse item views that are no longer visible to the user.

- LinearLayoutManager arranges the items in a one-dimensional list. Using a RecyclerView with LinearLayoutManager provides functionality like the older ListView layout.
- GridLayoutManager arranges the items in a two-dimensional grid, like the squares on a checkerboard. Using a RecyclerView with GridLayoutManager provides functionality like the older GridView layout.
- StaggeredGridLayoutManager arranges the items in a two-dimensional grid, with each column slightly offset from the one before, like the stars in an American flag.

Finally, we call ``setAdapter()`` and pass the customAdapter we created above (adapter) 

Learn More about RecyclerView
===
* [Releases](https://developer.android.com/jetpack/androidx/releases/recyclerview)
* [Codelab](https://developer.android.com/codelabs/android-training-create-recycler-view)
* [Create dynamic lists with RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
* [Advanced RecyclerView customization](https://developer.android.com/guide/topics/ui/layout/recyclerview-custom)

