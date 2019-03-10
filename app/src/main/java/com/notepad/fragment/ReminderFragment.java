package com.notepad.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notepad.R;

public class ReminderFragment extends Fragment {
    public static ReminderFragment newInstance() {

        Bundle args = new Bundle();

        ReminderFragment fragment = new ReminderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reminder, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(ViewGroup rootView) {
    }
}
