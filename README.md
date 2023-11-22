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

and in ```fragment_b.xml``` accordingly but also add an id ```content``` to the textview:

```xml
        android:id="@+id/content"
        android:text="@string/hello_fragment_b" />
```

and fill the string variables in ```strings.xml``` accordingly.
We will need the ```content``` id later.


Create the Pager Adapter
========================
Create a kotlin file ```PagerAdapter.kt``` with the class definition:

```kotlin
class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = ArrayList<Fragment>()
    private val pageIds = fragmentList.map { it.hashCode().toLong() }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyItemChanged(itemCount -1)
    }

    fun removeFragment(fragment: Fragment) {
        val position = getPosition(fragment)
        fragmentList.remove(fragment)
        notifyItemRemoved(position)
    }

    fun getPosition(fragment: Fragment): Int {
        return fragmentList.indexOf(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return pageIds.contains(itemId)
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
    companion object {
        var pagerAdapter: PagerAdapter? = null
        var viewPager: ViewPager2? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // overwrite appbar title
        this.title = "My title"

        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter!!.addFragment(FragmentA())

        viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager!!.adapter = pagerAdapter
    }
}
```

Note that ```pagerAdapter``` is defined as a companion object, so we can access and modify it from within the fragments.


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
        android:id="@+id/action_remove_this_fragment"
        android:orderInCategory="100"
        android:title="Remove this fragment"
        app:showAsAction="never" />
</menu>
```

Add to the FragmentA:

```kotlin
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        ...
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_a, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_fragment_b -> {
                // add function call here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
```

and to the FragmentB:

```kotlin
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        ...
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_b menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove_this_fragment -> {
                // add function call here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
```

Enable runtime fragment change
==============================

Add create fragment to the menu actions
---------------------------------------
Fill the action in ```onOptionsItemSelected``` in FragmentA and add a function to create a unique id string:

```kotlin
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_fragment_b -> {
                MainActivity.pagerAdapter!!.addFragment(FragmentB.newInstance(getUUID()))
                // show new page
                MainActivity.viewPager!!.currentItem = MainActivity.pagerAdapter!!.itemCount-1
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getUUID() = UUID.randomUUID().toString()
```

Change the content of FragmentB to:

```kotlin
class FragmentB : Fragment() {
    private var uuidString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuidString = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_b, container, false)

        val uuid = view.findViewById(R.id.content) as TextView
        uuid.text = uuidString

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_b, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove_this_fragment -> {
                MainActivity.pagerAdapter!!.removeFragment(this)
                // show most right page
                MainActivity.viewPager!!.currentItem = MainActivity.pagerAdapter!!.itemCount-1
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(uuidString: String) =
            FragmentB().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, uuidString)
                }
            }
    }
}
```


That's it!
