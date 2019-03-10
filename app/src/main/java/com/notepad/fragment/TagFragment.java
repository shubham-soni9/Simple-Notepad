package com.notepad.fragment;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notepad.NoteListActivity;
import com.notepad.R;
import com.notepad.Transition;
import com.notepad.adapter.TagsAdapter;
import com.notepad.appdata.Codes;
import com.notepad.database.Hashtag;
import com.notepad.database.Note;
import com.notepad.database.NoteViewModel;
import com.notepad.util.Utils;

import java.util.List;

import static com.notepad.appdata.Keys.Extras.HASH_TAG;

public class TagFragment extends Fragment implements TagsAdapter.OnTagSelectedListener {
    private RecyclerView  rvTagList;
    private TagsAdapter   tagsAdapter;
    private NoteViewModel noteViewModel;
    private Context       context;
    private List<Hashtag> hashtagList;
    private List<Note>    noteList;

    public static TagFragment newInstance() {
        Bundle args = new Bundle();
        TagFragment fragment = new TagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        //initialize();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tag, container, false);
        initViews(rootView);
        // initialize();
        return rootView;
    }


//    private void initialize() {
//        noteViewModel.insertAllTags(new Hashtag(Utils.getUniqueId(),"#Shopping"),
//                                    new Hashtag(Utils.getUniqueId(),"#Grossary"),
//                                    new Hashtag(Utils.getUniqueId(),"#Urgent"),
//                                    new Hashtag(Utils.getUniqueId(),"#Weekend"));
    //  }

    private void initViews(ViewGroup rootView) {
        rvTagList = rootView.findViewById(R.id.rvTagList);
        noteViewModel.getAllTags().observe(this, new Observer<List<Hashtag>>() {
            @Override
            public void onChanged(@Nullable List<Hashtag> hashtags) {
                hashtagList = hashtags;
                setAdapter();
            }
        });
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteList = notes;
                setAdapter();
            }
        });
    }


    private void setAdapter() {
        if (noteList == null || hashtagList == null) {
            return;
        }
        String tagId;
        int count;

        for (Hashtag hashtag : hashtagList) {
            tagId = hashtag.getTagId();
            count = 0;
            for (Note note : noteList) {
                if(!Utils.isEmpty(note.getTagId())){
                if (note.getTagId().equalsIgnoreCase(tagId)) {
                    count++;
                }}
            }
            hashtag.setNotesCount(count);
        }

        rvTagList.setLayoutManager(new LinearLayoutManager(context));
        Utils.objecttoJson(hashtagList);
        if (tagsAdapter == null) {
            tagsAdapter = new TagsAdapter(this, hashtagList);
            rvTagList.setAdapter(tagsAdapter);
        } else {
            tagsAdapter.refreshAdapterSet(hashtagList);
        }
    }


    @Override
    public void onTagSelected(Hashtag hashtag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(HASH_TAG, hashtag);
        Transition.transitForResult((Activity) context, NoteListActivity.class, Codes.Request.NODE_LIST_ACTIVITY, bundle);
    }
}
