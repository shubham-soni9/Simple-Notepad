package com.notepad.appdata;

public interface Keys {
    interface ImagePickerType {
        int BOTH    = 0;
        int GALLERY = 2;
        int CAMERA  = 1;
    }

    interface Extras {
        String IS_SHOW_FAVORITE = "is_show_favorite";
        String HASH_TAG         = "hash_tag";
    }

    interface Prefs {
        String IS_FIRST_LOGIN = "is_first_login";
    }
}
