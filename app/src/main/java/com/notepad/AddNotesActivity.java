package com.notepad;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.notepad.adapter.ImageAdapter;
import com.notepad.appdata.Codes;
import com.notepad.appdata.Constant;
import com.notepad.database.Hashtag;
import com.notepad.database.Note;
import com.notepad.database.NoteViewModel;
import com.notepad.dialog.DialogWithListAndButtons;
import com.notepad.plugin.CustomScannerActivity;
import com.notepad.plugin.LinedEditText;
import com.notepad.plugin.colorpicker.ColorPicker;
import com.notepad.structure.BaseActivity;
import com.notepad.util.AssignUtils;
import com.notepad.util.DateUtils;
import com.notepad.util.ImageUtils;
import com.notepad.util.Log;
import com.notepad.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddNotesActivity extends BaseActivity implements View.OnClickListener {
    private final String              TAG                 = AddNotesActivity.class.getName();
    private       LinedEditText       etAddNote;
    private       ImageView           ivDone;
    private       ImageView           ivBack;
    private       ImageView           ivScanBarcode;
    private       ImageView           ivChangeColor;
    private       ImageView           ivAddGalaryImage;
    private       ImageView           ivAddCameraImage;
    private       ImageView           ivAddTag;
    private       EditText            etTitle;
    private       Note                currentNote;
    private       LinearLayout        rlDataParent;
    private       ImageUtils          imageUtils;
    private       ArrayList<String>   imageList;
    private       RecyclerView        rvImageList;
    private       ImageAdapter        imageAdapter;
    private       NoteViewModel       noteViewModel;
    private       String              currentImageId;
    private       CompositeDisposable compositeDisposable = new CompositeDisposable();
    private       List<Hashtag>       tagsList;
    private       TextWatcher         noteTextWatcher;
    private       TextView            tvLastEditedDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        init();
        prefilData();
        setImageAdapter();
        setTagAdapter();
        etAddNote.requestFocus();
        etAddNote.setSelection(etAddNote.getText().length());
    }

    private void setTitleListener() {
        if (Utils.isEmpty(etTitle)) {
            noteTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    etTitle.setText(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            etAddNote.addTextChangedListener(noteTextWatcher);
        }
        etTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setImageAdapter() {
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(this, imageList);
            rvImageList.setAdapter(imageAdapter);
        } else {
            imageAdapter.refreshAdapterSet(imageList);
        }
    }

    private void setTagAdapter() {
        noteViewModel.getAllTags().observe(this, tagsList -> {
            this.tagsList = tagsList;
        });
    }

    private void prefilData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String noteId = bundle.getString(Note.class.getName());
            if (!Utils.isEmpty(noteId)) {
                noteViewModel.getNoteById(noteId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Note>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Note note) {
                        Log.e(TAG, "on Note Get Query Success" + Utils.objecttoJson(note));
                        onNoteDataFetched(note);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        } else {
            currentNote = new Note();
            imageList = new ArrayList<>();
            tvLastEditedDate.setText(getString(R.string.edited) + " " + DateUtils.getInstance().getFormattedDate(new Date(), Constant.DateFormat.LAST_EDITED_TIME_FORMAT));
            setTitleListener();
        }
    }

    private void onNoteDataFetched(Note note) {
        this.currentNote = note;
        if (note != null) {
            etTitle.setText(note.getTitle());
            etAddNote.setText(Utils.assign(note.getTextData()));
            etAddNote.setSelection(Utils.assign(note.getTextData()).length());
            if (note.getNoteColor() != 0) {
                rlDataParent.setBackgroundColor(note.getNoteColor());
            }
            imageList = note.getImageList();
            setImageAdapter();
            setTitleListener();
            tvLastEditedDate.setText(getString(R.string.edited) + " " + DateUtils.getInstance().getFormattedDate(note.getLastUpdatedTime(), Constant.DateFormat.LAST_EDITED_TIME_FORMAT));
        }
    }

    private void init() {
        etAddNote = findViewById(R.id.etAddNote);
        ivDone = findViewById(R.id.iv_done);
        ivBack = findViewById(R.id.iv_add_note_back);
        ivScanBarcode = findViewById(R.id.ivScanBarcode);
        ivChangeColor = findViewById(R.id.ivChangeColor);
        rlDataParent = findViewById(R.id.rlDataParent);
        etTitle = findViewById(R.id.add_note_et_title);
        ivAddGalaryImage = findViewById(R.id.ivAddGalaryImage);
        ivAddCameraImage = findViewById(R.id.ivAddCameraImage);
        rvImageList = findViewById(R.id.rvImageList);
        ivAddTag = findViewById(R.id.ivAddTag);
        tvLastEditedDate = findViewById(R.id.tvLastEditedDate);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        rvImageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Utils.setOnClickListener(this, ivDone, ivBack, ivScanBarcode, ivChangeColor, ivAddCameraImage, ivAddGalaryImage, ivAddTag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_done:
                saveNote();
                break;
            case R.id.iv_add_note_back:
                performBackAction();
                break;
            case R.id.ivScanBarcode:
                scanBarcode();
                break;
            case R.id.ivChangeColor:
                openColorDialog();
                break;
            case R.id.ivAddGalaryImage:
                openGallery();
                break;
            case R.id.ivAddCameraImage:
                startCamera();
                break;
            case R.id.ivAddTag:
                onTagClicked();
                break;
        }
    }

    private void onTagClicked() {
        DialogWithListAndButtons
                .with(this)
                .show(getString(R.string.select_tag), new ArrayList<>(tagsList), true,
                      (clickedPosition, clickedPositionText) -> {
                          currentNote.setTag(clickedPositionText);
                      }, currentNote.getTagId());
    }


    private void openGallery() {
        if (imageUtils == null) {
            imageUtils = new ImageUtils(this);
        }
        currentImageId = Utils.getUniqueId() + ".jpg";
        imageUtils.openGallery();
    }

    private void startCamera() {
        if (imageUtils == null) {
            imageUtils = new ImageUtils(this);
        }
        currentImageId = Utils.getUniqueId() + ".jpg";
        imageUtils.startCamera(currentImageId);
    }

    private void openColorDialog() {
        final ColorPicker colorPicker = new ColorPicker(AddNotesActivity.this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                currentNote.setNoteColor(color);
                rlDataParent.setBackgroundColor(color);
            }

            @Override
            public void onCancel() {
            }
        })
                .setDefaultColorButton(Color.parseColor("#f84c44"))
                .setRoundColorButton(true)
                .setColumns(5)
                .show();
    }

    private void scanBarcode() {
        new IntentIntegrator(AddNotesActivity.this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }

    private void compressAndSaveImageBitmap() {
        try {
            if (imageUtils == null) {
                imageUtils = new ImageUtils(this);
            }
            String fileName = imageUtils.compressImage(currentImageId);
            if (Utils.isEmpty(fileName)) {
                Utils.snackBar(this, getString(R.string.could_not_read_from_source_text), getWindow().getDecorView().findViewById(android.R.id.content), Codes.SnackBarType.ERROR);
            } else {
                imageList.add(fileName);
                setImageAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.snackBar(this, getString(R.string.could_not_read_from_source_text), Codes.SnackBarType.MESSAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Codes.Request.OPEN_CAMERA_ADD_IMAGE:
                    compressAndSaveImageBitmap();
                    break;
                case Codes.Request.OPEN_GALLERY_ADD_IMAGE:
                    if (imageUtils == null) {
                        imageUtils = new ImageUtils(this);
                    }
                    try {
                        imageUtils.copyFileFromUri(data.getData(), currentImageId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    compressAndSaveImageBitmap();
                    break;
                default:
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        if (result.getContents() != null) {
                            etAddNote.append("\n" + result.getContents());
                        }
                    }
                    break;
            }
        }
    }

    private void performBackAction() {
        Utils.hideSoftKeyboard(this);
        Transition.exit(this);
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

    private void saveNote() {
        setResult(RESULT_OK);
        if (Utils.isEmpty(currentNote.getNoteId())) {
            currentNote.setNoteId(Utils.getUniqueId());
        }
        currentNote.setLastUpdatedTime(System.currentTimeMillis());
        currentNote.setTextData(AssignUtils.get(etAddNote));
        currentNote.setTitle(AssignUtils.get(etTitle));
        currentNote.setImageList(imageList);
        noteViewModel.insert(currentNote);
        performBackAction();
    }
}
