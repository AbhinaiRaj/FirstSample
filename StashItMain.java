package com.mobicloudstashitnownew.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobicloud.stashitnownew.R;
import com.mobicloudstashitnownew.fragments.BookingsReceived;
import com.mobicloudstashitnownew.fragments.Favourites;
import com.mobicloudstashitnownew.fragments.Feedback;
import com.mobicloudstashitnownew.fragments.HomePage;
import com.mobicloudstashitnownew.fragments.ListYourSpace;
import com.mobicloudstashitnownew.fragments.LogIn;
import com.mobicloudstashitnownew.fragments.NotificationsFragment;
import com.mobicloudstashitnownew.fragments.Payment;
import com.mobicloudstashitnownew.fragments.ProviderListings;
import com.mobicloudstashitnownew.fragments.ReferAFriend;
import com.mobicloudstashitnownew.fragments.SettingsInfo;
import com.mobicloudstashitnownew.otherinfo.CircleTransform;
import com.mobicloudstashitnownew.utils.SharedPref;
import com.mobicloudstashitnownew.volleyApiCalling.AppController;

public class StashItMain extends AppCompatActivity
{
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtviewprofile;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "http://www.test4mcis.com/map/map/pexels-photo-106399.jpeg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_NOTIFICATIONS = "home";
    private static final String TAG_REFERFRIEND = "home";
    private static final String TAG_LISTYOURSPACE = "listyourspace";
    private static final String TAG_FEEDBACK = "feedback";
    private static final String TAG_BOOKINGSRECEIVED = "bookinghistory";
    private static final String TAG_PROVIDERLISTINGS= "providerlistings";
    private static final String TAG_FAVOURITES= "favourites";
    private static final String TAG_PAYMENT = "payment";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_LOGIN= "login";


    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    String latiloninfo;
    double latidetails,longidetails;
    private boolean islogin;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stash_it_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHandler = new Handler();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black));

//        Bundle bun=getIntent().getExtras();
//        int val=bun.getInt("VAL");

//        if(val==1)
//        {
//            note1.setVisibility(View.VISIBLE);
//
//        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtviewprofile = (TextView) navHeader.findViewById(R.id.viewprofile);

        txtviewprofile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent viewintent=new Intent(StashItMain.this,ViewProfile.class);
                startActivity(viewintent);
            }
        });
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        Menu nav_Menu = navigationView.getMenu();
        //nav_Menu.findItem(R.id.nav_editprofile).setVisible(false);
        // Toast.makeText(getApplicationContext(),AppController.getPreference().getString(SharedPref.USERID),Toast.LENGTH_LONG).show();
        if(AppController.getPreference().getString(SharedPref.USERID).equals(""))
        {
            nav_Menu.findItem(R.id.nav_home).setVisible(false);
        }



        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(AppController.getPreference().getString(SharedPref.USERID).equals(""))
        {
            getMenuInflater().inflate(R.menu.main_signin, menu);
            MenuItem signin = menu.findItem(R.id.signin);
            signin.setVisible(true);
        }
        else
        {
            MenuItem signin = menu.findItem(R.id.signin);

        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.signin:
            {
                Intent it=new Intent(StashItMain.this,InformationPage.class);
                startActivity(it);

            }



        }
        return true;
    }

    private void loadNavHeader()
    {
        // name, website
      txtName.setText("StashItNow");
      txtviewprofile.setText("view profile");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(R.drawable.profile_image)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
//            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
//        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment()
    {
        switch (navItemIndex) {
            case 0:
                // home fragment
                HomePage homeFragment = new HomePage();
                return homeFragment;


            case 1:
                // home fragment
                NotificationsFragment notificationfragment = new NotificationsFragment();
                return notificationfragment;


            case 2:
                // home fragment
                ReferAFriend referfriendfragment = new ReferAFriend();
                return referfriendfragment;

            case 3:
                // listyourspace fragment
                ListYourSpace list_space = new ListYourSpace();
                return list_space;



            case 4:
                // notification fragment
                Feedback feedBack= new Feedback();
                return feedBack;
            case 5:
                // bookinghistory fragment
                BookingsReceived bookingsReceived = new BookingsReceived();
                return bookingsReceived;

            case 6:
                // ProviderListings fragment
                ProviderListings providerListings = new ProviderListings();
                return providerListings;

            case 7:
                // Favourites fragment
                Favourites favourites = new Favourites();
                return favourites;
            case 8:
                // payment fragment
                Payment payment = new Payment();
                return payment;

//            case 7:
//                // Help fragment
//                Help help = new Help();
//                return help;
//


//            case 8:
//                // settings fragment
//                EditProfile editprofileFragment = new EditProfile();
//                return editprofileFragment;
////
            case 9:
                // editprofile fragment
                SettingsInfo settingsInfo = new SettingsInfo();
                return settingsInfo;

//            case 10:
//                // logout fragment
//                LogOut logOut = new LogOut();
//                return logOut;

            case 10:
                // logout fragment
                LogIn logIn = new LogIn();
                return logIn;

            default:

                return new HomePage();
        }
    }

    private void setToolbarTitle()
    {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu()
    {
//        navigationView.getMenu().getItem(navItemIndex).setChecked(true);

        if(AppController.getPreference().getString(SharedPref.USERID).equals(""))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_login);
        } else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_logout);
        }
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    case R.id.nav_notifications:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;

                    case R.id.nav_referfriend:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_REFERFRIEND;
                        break;

                    case R.id.nav_listyourspace:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LISTYOURSPACE;
                        break;
                    case R.id.nav_feedback:
                        navItemIndex = 4;
                        CURRENT_TAG =TAG_FEEDBACK;
                        break;
                    case R.id.nav_bookingsreceived:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_BOOKINGSRECEIVED;
                        break;
                    case R.id.nav_providerlistings:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_PROVIDERLISTINGS;
                        break;
                    case R.id.nav_favourites:
                        navItemIndex = 7;
                        CURRENT_TAG =TAG_FAVOURITES;
                        break;
                    case R.id.nav_payment:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_PAYMENT;
                        break;


                    case R.id.nav_settings:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
//
                    case R.id.nav_login:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_LOGIN;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                {
                    menuItem.setChecked(false);
                }
                else
                {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

}