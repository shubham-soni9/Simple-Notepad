package com.notepad;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.notepad.adapter.HomePagerAdapter;
import com.notepad.appdata.Codes;
import com.notepad.appdata.Constant;
import com.notepad.database.Hashtag;
import com.notepad.database.NoteViewModel;
import com.notepad.dialog.TagInputDialog;
import com.notepad.structure.BaseActivity;
import com.notepad.util.Log;
import com.notepad.util.Utils;

import static com.notepad.appdata.Codes.Request.OPEN_ADD_NOTES_ACTIVITY;

public class HomeActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private final String               TAG = HomeActivity.class.getName();
    private       DrawerLayout         drawer;
    private       BottomNavigationView navigationView;
    private       View                 mainContent;
    private       ViewPager            vpHome;
    private       FloatingActionButton fabAddNote;
    private       TextView             tvTitle;
    private       HomePagerAdapter     homePagerAdapter;
    private       NoteViewModel        noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setListener();
        setData(Constant.HomeNavigation.HOME);
    }

    private void init() {
        drawer = findViewById(R.id.activity_home_dl_main);
        navigationView = findViewById(R.id.bottom_navigation);
        mainContent = findViewById(R.id.main_content);
        vpHome = findViewById(R.id.vp_home);
        fabAddNote = findViewById(R.id.home_fab_add);
        tvTitle = findViewById(R.id.home_tv_menu);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        Utils.setOnClickListener(this, findViewById(R.id.home_iv_menu), fabAddNote);
    }

    private void setListener() {
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int pos) {
                Log.e(TAG, "On Page Selected :: " + pos);
                tvTitle.setText(Constant.HomeNavigation.getItemByPosition(vpHome.getCurrentItem()).titleResId);
                updateButtonUI();
                if (pos != Constant.HomeNavigation.getItemByResId(navigationView.getSelectedItemId()).position) {
                    Log.e(TAG, "On Page Applied :: " + pos);
                    navigationView.setSelectedItemId(Constant.HomeNavigation.getItemByPosition(pos).resourceId);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private void updateButtonUI() {
        int i = vpHome.getCurrentItem();
        if (i == Constant.HomeNavigation.REMINDER.position) {
            fabAddNote.hide();
            Log.e(TAG, "Button UI :: Reminder");
        } else if (i == Constant.HomeNavigation.TAG.position) {
            fabAddNote.hide();
            fabAddNote.setImageResource(R.drawable.ic_tags);
            fabAddNote.show();
            Log.e(TAG, "Button UI :: Hashtag");
        } else if (i == Constant.HomeNavigation.HOME.position) {
            fabAddNote.hide();
            fabAddNote.setImageResource(R.drawable.ic_floating_button);
            fabAddNote.show();
            Log.e(TAG, "Button UI :: Home");
        } else if (i == Constant.HomeNavigation.FAVORITE.position) {
            fabAddNote.hide();
            fabAddNote.setImageResource(R.drawable.ic_floating_button);
            fabAddNote.show();
            Log.e(TAG, "Button UI :: Favorite");
        } else if (i == Constant.HomeNavigation.CALENDER.position) {
            fabAddNote.hide();
            Log.e(TAG, "Button UI :: Calender");
        }
    }

    private void setData(Constant.HomeNavigation homeNavigation) {
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        vpHome.setAdapter(homePagerAdapter);
        tvTitle.setText(Constant.HomeNavigation.getItemByPosition(vpHome.getCurrentItem()).titleResId);
        vpHome.setCurrentItem(homeNavigation.position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_iv_menu:
                openDrawer();
                break;
            case R.id.home_fab_add:
                onFabClicked();
                break;
        }

    }

    private void onFabClicked() {
        int i = vpHome.getCurrentItem();
        if (i == Constant.HomeNavigation.REMINDER.position) {
            Log.e(TAG, "Fab Button Clicked :: Reminder");
        } else if (i == Constant.HomeNavigation.TAG.position) {
            addTag();
            Log.e(TAG, "Fab Button Clicked :: Hashtag");
        } else if (i == Constant.HomeNavigation.HOME.position) {
            addNote();
            Log.e(TAG, "Fab Button Clicked  :: Home");
        } else if (i == Constant.HomeNavigation.FAVORITE.position) {
            addNote();
            Log.e(TAG, "Fab Button Clicked  :: Favorite");
        } else if (i == Constant.HomeNavigation.CALENDER.position) {
            Log.e(TAG, "Fab Button Clicked  :: Calender");
        }
    }

    private void addTag() {
        new TagInputDialog.Builder(this)
                .hint(getString(R.string.enter_tag))
                .positiveButton(getString(R.string.save))
                .negativeButton(getString(R.string.cancel_text))
                .listener(new TagInputDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        noteViewModel.insertAllTags(new Hashtag(Utils.getUniqueId(), backpack.getString(TagInputDialog.MESSAGE)));
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {

                    }
                }).build().show();
    }

    private void addNote() {
        Transition.transitForResult(this, AddNotesActivity.class, OPEN_ADD_NOTES_ACTIVITY, null, false);
    }
    private void openDrawer() {
        drawer.openDrawer(Gravity.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.e(TAG, "On Navigation Item Selected");
        if (menuItem.getItemId() != Constant.HomeNavigation.getItemByPosition(vpHome.getCurrentItem()).resourceId) {
            Log.e(TAG, "On Navigation Item Applied");
            vpHome.setCurrentItem(Constant.HomeNavigation.getItemByResId(menuItem.getItemId()).position);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Codes.Request.OPEN_ADD_NOTES_ACTIVITY:
                    setData(Constant.HomeNavigation.HOME);
                    break;
            }
        }
    }


}
