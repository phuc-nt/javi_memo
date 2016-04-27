package com.ytasia.dict.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.video.VideoListener;
import com.ytasia.dict.dao.db_handle.SuggestEntryAccess;
import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiHandler;
import com.ytasia.dict.dao.db_handle.TBUserHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.UserObj;
import com.ytasia.dict.service.AuthenService;
import com.ytasia.dict.service.EntryService;
import com.ytasia.dict.util.YTDictValues;
import com.ytasia.dict.view.fragment.EntryListFragment;
import com.ytasia.dict.view.fragment.KanjiListFragment;
import com.ytasia.dict.view.fragment.QuizFragment;
import com.ytasia.dict.view.fragment.SettingFragment;

import ytasia.dictionary.R;

public class MainActivity extends AppCompatActivity {

    private final int QUIZ_FRAGMENT_POSITION = 0;
    private final int ENTRY_FRAGMENT_POSITION = 1;
    private final int KANJI_FRAGMENT_POSITION = 2;
    private final int SETTING_FRAGMENT_POSITION = 3;

    public static final int REQUEST_CODE_ENTRY_ADD = 101;
    public static final int REQUEST_CODE_ENTRY_EDIT = 102;
    public static final int RESULT_CODE_ENTRY_ADD = 201;
    public static final int RESULT_CODE_ENTRY_EDIT = 202;

    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;

    private StartAppAd startAppAd = new StartAppAd(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set app context is MainActivity
        YTDictValues.appContext = MainActivity.this;

        // init Ad
        StartAppSDK.init(this, "103367364", "203600019", true);


//        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);
//        startAppAd.setVideoListener(new VideoListener() {
//            @Override
//            public void onVideoCompleted() {
//
//            }
//        });
//        startAppAd.showSplash(this, savedInstanceState);

        // Authentication to server
        authenToServer();

        // set data for YTDictValues
        setYTDictValues();

        int fragmentPosition = 0;

        Intent i = getIntent();
        if (i.getExtras() != null) {
            String fragmentPositionString = i.getStringExtra("fragmentPosition");
            if (fragmentPositionString != null) {
                fragmentPosition = Integer.parseInt(fragmentPositionString);
            }
        }
        mainTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mainViewPager = (ViewPager) findViewById(R.id.main_viewpager);


        setupViewPager(mainViewPager);

        switch (fragmentPosition) {
            case QUIZ_FRAGMENT_POSITION:
                mainViewPager.setCurrentItem(0);
                break;
            case ENTRY_FRAGMENT_POSITION:
                mainViewPager.setCurrentItem(1);
                break;
            case KANJI_FRAGMENT_POSITION:
                mainViewPager.setCurrentItem(2);
                break;
            case SETTING_FRAGMENT_POSITION:
                mainViewPager.setCurrentItem(3);
                break;
        }

        mainTabLayout.setupWithViewPager(mainViewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
    }

    /**
     * Setup 4 main fragments to ViewPager
     *
     * @param v ViewPager
     */
    private void setupViewPager(ViewPager v) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new QuizFragment(), getResources().getString(R.string.quiz_tab_label));
        adapter.addFragment(new EntryListFragment(), getResources().getString(R.string.entryList_tab_label));
        adapter.addFragment(new KanjiListFragment(), getResources().getString(R.string.kanjiList_tab_label));
        adapter.addFragment(new SettingFragment(), getResources().getString(R.string.setting_tab_label));

        v.setAdapter(adapter);
    }

    /**
     * Customized PagerAdapter
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    /**
     * Authenticate to server by User info after Login
     */
    private void authenToServer() {
        final String name;
        final String type;
        final String pass;

        if (YTDictValues.fUserid != null) { // facebook user
            YTDictValues.username = YTDictValues.fUserid;
            YTDictValues.acc_type = "f";
            Log.i("Facebook id", YTDictValues.username);
        } else if (YTDictValues.gUserid != null) { // google user
            YTDictValues.username = YTDictValues.gUserid;
            YTDictValues.acc_type = "g";
            Log.i("Google id", YTDictValues.username);
        }

        name = YTDictValues.username;
        type = YTDictValues.acc_type;
        pass = UUID.randomUUID().toString();

        YTDictValues.user = new UserObj(0, name, pass);

        // connect to server
        final Object ob = new Object();
        final String[] ret = new String[6];
        final boolean[] locked = {false};
        new Thread() {
            public void run() {

                try {
                    YTDictValues.username = name;
                    synchronized (ob) {
                        locked[0] = true;
                        ret[0] = getLoginInfo(name, pass, type);
                        ob.notify();
                    }
                    Log.v("Login button onclick", ret[0]);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    synchronized (ob) {
                        ob.notify();
                    }
                }
            }

        }.start();

        synchronized (ob) {
            try {
                ob.wait();
                if (ret[0] != null) {
                    //pass = ret[0];
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Get login info from server
     *
     * @param name
     * @param pwd
     * @param type
     * @return
     * @throws IOException
     */
    private String getLoginInfo(String name, String pwd, String type) throws IOException {
        AuthenService ds = new AuthenService();
        String uuid = "";
        try {
            uuid = ds.getLoginInfo(name, pwd, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }


    /**
     * Set data for [entriesContent] and [kanjiEntryIds]
     * on util.YTDictValues
     */
    private void setYTDictValues() {
        TBEntryHandler entryHandler = new TBEntryHandler(this);
        List<EntryObj> entryObjs = entryHandler.getAll();
        if (!entryObjs.isEmpty()) {
            for (int i = 0; i < entryObjs.size(); i++) {
                YTDictValues.entriesContent.add(i, entryObjs.get(i).getContent());
            }
        }
    }

    public void onShowAdButtonPressed() {
        startAppAd.showAd();
    }

    /**
     * From here is for TEST
     * ---------------------------------------------------------------------------------------------
     */

    /*private void testDB() {
        TBUserHandler hd = new TBUserHandler(this);
        hd.dropAllTables();

        // test TBUserHandler
        UserObj obj1 = new UserObj("duyla@yz-japan.tokyo", "1234");
        hd.add(obj1);
        UserObj obj2 = new UserObj("tanle@yz-japan.tokyo", "1234");
        hd.add(obj2);
        List<UserObj> ls = hd.getAll();
        Assert.assertEquals(ls.size(), 2);
        Assert.assertEquals(ls.get(0).getUserId(), 1);
        Assert.assertEquals("duyla@yz-japan.tokyo", ls.get(0).getEmail());
        Assert.assertEquals(0, ls.get(0).getEntryHighScore());
        Assert.assertEquals(ls.get(1).getUserId(), 2);
        Assert.assertEquals("tanle@yz-japan.tokyo", ls.get(1).getEmail());


        // test TBEntryHandler

        int id = ls.get(1).getUserId();
        TBEntryHandler entryHd = new TBEntryHandler(this);

        EntryObj entryObj1 = new EntryObj(id, "Content1", "Furigana", "Meaning", "Example", "Source");
        entryHd.add(entryObj1, this);

        EntryObj entryObj2 = new EntryObj(id, "Content2", "Furigana2", "Meaning2", "Example2", "Source");
        entryHd.add(entryObj2, this);

        List<EntryObj> ls2 = entryHd.getAll();
        Assert.assertEquals(ls2.size(), 2);
        Assert.assertEquals(ls2.get(0).getEntryId(), 1);
        Assert.assertEquals(ls2.get(0).getLevel(), 1);
        Assert.assertEquals("Content2", ls2.get(1).getContent());

        entryHd.delete(this, ls2.get(0).getEntryId());
        List<EntryObj> ls7 = entryHd.getAll();
        Assert.assertEquals(1, ls7.size());

        *//*EntryObj entryObj3 = new EntryObj(0, id, "Content2-FIX", "Furigana2-FIX", "Meaning2-FIX", "Example2", 2,
                "Source", new Date(new java.util.Date().getTime()));
        entryHd.update(entryObj3, 2);
        List<EntryObj> ls5 = entryHd.getAll();
        Assert.assertEquals(2, ls5.size());
        Assert.assertEquals(2, ls5.get(1).getEntryId());
        Assert.assertEquals(2, ls5.get(1).getLevel());
        Assert.assertEquals("Content2-FIX", ls5.get(1).getContent());*//*


        // test TBKanjiHandler
        TBKanjiHandler kanjiHd = new TBKanjiHandler(this);
        KanjiObj kanjiObj1 = new KanjiObj(0, '心', "こころ", "シン", "TAM", "qua tim", "associated", 3);
        kanjiHd.add(kanjiObj1);
        KanjiObj kanjiObj2 = new KanjiObj(0, '石', "いし", "セキ", "DA", "hon da", "associated", 2);
        kanjiHd.add(kanjiObj2);

        List<KanjiObj> ls3 = kanjiHd.getAll();
        Assert.assertEquals(ls3.size(), 2);
        Assert.assertEquals(ls3.get(0).getKanjiId(), 1);
        Assert.assertEquals('心', ls3.get(0).getCharacter());
        Assert.assertEquals("いし", ls3.get(1).getOnyomi());

        // test TBKanjiEntryHandler
        TBKanjiEntryHandler ekHd = new TBKanjiEntryHandler(this);
        KanjiEntryObj kanjiEntryObj1 = new KanjiEntryObj(ls2.get(0).getEntryId(), ls3.get(0).getKanjiId());
        KanjiEntryObj kanjiEntryObj2 = new KanjiEntryObj(ls2.get(1).getEntryId(), ls3.get(1).getKanjiId());
        ekHd.add(kanjiEntryObj1);
        ekHd.add(kanjiEntryObj2);

        List<KanjiEntryObj> ls4 = ekHd.getAll();

        Assert.assertEquals(ls4.size(), 2);
        Assert.assertEquals(ls4.get(0).getKanjiId(), ls3.get(0).getKanjiId());
        Assert.assertEquals(ls4.get(0).getEntryId(), ls2.get(0).getEntryId());
        System.out.println("DONE!");
    }*/
    private void createSampleDb() {
        YTDictValues.appContext = this.getApplicationContext();
        // DBBasic db = DBBasic.createInstance();

        TBKanjiHandler kanjiHd = new TBKanjiHandler(this);
        TBEntryHandler entryHd = new TBEntryHandler(this);
        TBUserHandler hd = new TBUserHandler(this);

        EntryService entryService = new EntryService();

        kanjiHd.dropAllTables();
        entryHd.dropAllTables();
        hd.dropAllTables();

        final String userName = "phucnt@yz-japan.tokyo";
        final String pass = "phucnt123";
        String userId;

        YTDictValues.user = new UserObj(0, userName, pass);


        EntryObj ob1, ob2, ob3, ob4, ob5, ob6, ob7, ob8, ob9, ob10;

        ob1 = new EntryObj(this, userName, "彼女");
        ob2 = new EntryObj(this, userName, "家族");
       /* ob3 = new EntryObj(this, userName, "お兄さん");
        ob4 = new EntryObj(this, userName, "お姉さん");
        ob5 = new EntryObj(this, userName, "人間");
        ob6 = new EntryObj(this, userName, "公園");
        ob7 = new EntryObj(this, userName, "財布");
        ob8 = new EntryObj(this, userName, "社長");
        ob9 = new EntryObj(this, userName, "独身");
        ob10 = new EntryObj(this, userName, "親切");*/

        ob1.setLevel(1);
        ob2.setLevel(4);
        /*ob3.setLevel(2);
        ob4.setLevel(0);
        ob5.setLevel(0);
        ob6.setLevel(1);
        ob7.setLevel(0);
        ob8.setLevel(2);
        ob9.setLevel(2);
        ob10.setLevel(0);*/

        //entryService.add(ob1);
        //entryService.add(ob2);

        //KanjiEntryService service = new KanjiEntryService();
        //service.add("1", "entry1");
        //Runtime.getRuntime().
        //System.in.read();
        //service.add("2", "entry2");
        //service.add("3", "entry3");


        /*entryHd.add(ob1, this);
        entryHd.add(ob2, this);
        entryHd.add(ob3, this);
        entryHd.add(ob4, this);
        entryHd.add(ob5, this);
        entryHd.add(ob6, this);
        entryHd.add(ob7, this);
        entryHd.add(ob8, this);
        entryHd.add(ob9, this);
        entryHd.add(ob10, this);*/
    }


    private void testSuggestDb() {
        SuggestEntryAccess dbAccess = SuggestEntryAccess.getInstance(this);
        dbAccess.open();
        System.out.println(Html.fromHtml(dbAccess.getSuggestMeaning("家族")));
        dbAccess.close();
    }

}
