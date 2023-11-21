# Viewpager2WithFragmentStateAdapter
Android Kotlin example to create a pager with dynamic fragment use and individual app bar menus.

Setting up the project
======================
In Android Studio create a new project from template "Basic View Activity".

Create two fragments
====================
Run ```New->Fragment->Fragment (blank)``` and name it ```FragmentA```.

Run ```New->Fragment->Fragment (blank)``` and name it ```FragmentB```.

Change the hello string in ```fragment_a.xml``` to:

```xml
android:text="@string/hello_fragment_a" />
```

and in ```fragment_b.xml``` to:

```xml
android:text="@string/hello_fragment_b" />
```

and fill the string variables in ```strings.xml``` accordingly.


Create the Pager Adapter
========================
Create a kotlin file ```PagerAdapter.kt``` with the class definition:

```kotlin
class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = ArrayList<Fragment>()
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}
```

Set the theme
=============
Edit ```res/values/themes.xml``` as follows:

```xml
    <style name="Theme.Viewpager2WithFragmentStateAdapter" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```

Add the color definition to ```res/values/colors.xml```:

```xml
    <color name="colorPrimary">#ffcb65</color>
    <color name="colorPrimaryDark">#303F9F</color>
    <color name="colorAccent">#3F51B5</color>
```

Create the main activity view
=============================
Create a ```res```directory ```layout``` with the layout resource file ```activity_main.xml```:

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".MainActivity">

    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

Update the Main Activity:
=========================

```kotlin
class MainActivity : AppCompatActivity() {
    var pagerAdapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // overwrite appbar title
        this.title = "My title"

        val viewPager: ViewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter!!.addFragment(FragmentA())
        pagerAdapter!!.addFragment(FragmentB())
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                invalidateOptionsMenu(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        invalidateOptionsMenu(0)
    }

    private fun invalidateOptionsMenu(position: Int) {
        for (i in 0 until (pagerAdapter?.itemCount ?: 0)) {
            val fragment: Fragment? = pagerAdapter?.createFragment(i)
            fragment?.setHasOptionsMenu(i == position)
        }
        invalidateOptionsMenu()
    }
}
```

Add the menus
=============
Create a directorty ```menu``` in ```res```.

Create menu resource file ```menu_fragment_a.xml```:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/action_add_fragment_b"
        android:orderInCategory="100"
        android:title="Add fragment B"
        app:showAsAction="never" />
</menu>
```

and ```menu_fragment_b.xml```:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/action_add_fragment_a"
        android:orderInCategory="100"
        android:title="Add fragment A"
        app:showAsAction="never" />
</menu>
```

Add to the FragmentA:
```kotlin
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_a, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_fragment_a -> {
                // add function call here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
```

and to the FragmentB:
```kotlin
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_b menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_fragment_b -> {
                // add function call here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
```

Enable runtime fragment change
==============================

Add the listener to the page adapter
------------------------------------
Add ```notifyDataSetChanged()``` to the ```addFragment``` function in our ```PagerAdapter.kt```:

```kotlin
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }
```

Add create fragment to the menu actions
---------------------------------------
Fill the action in ```onOptionsItemSelected``` in FragmentA:

```kotlin
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_fragment_b -> {
                pagerAdapter!!.addFragment(FragmentB())
                val fragmentNum = pagerAdapter?.itemCount ?: 0
                pagerAdapter?.createFragment(fragmentNum-1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
```

and in FragmentB:

```kotlin
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_fragment_a -> {
                pagerAdapter!!.addFragment(FragmentA())
                val fragmentNum = pagerAdapter?.itemCount ?: 0
                pagerAdapter?.createFragment(fragmentNum-1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
```
