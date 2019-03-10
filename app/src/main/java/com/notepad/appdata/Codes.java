package com.notepad.appdata;

public interface Codes {

    interface Request {
        int OPEN_ADD_NOTES_ACTIVITY = 101;
        int OPEN_CAMERA_ADD_IMAGE   = 102;
        int OPEN_GALLERY_ADD_IMAGE  = 103;
        int NODE_LIST_ACTIVITY      = 104;
    }

    /**
     * Stores all the Codes that differentiate the Permissions
     */
    interface Permission {

        int LOCATION                           = 1;
        int CAMERA                             = 2;
        int READ_FILE                          = 3;
        int OPEN_GALLERY                       = 4;
        int SAVE_BITMAP                        = 5;
        int READ_PHONE_STATE                   = 6;
        int READ_AUDIO_FILE                    = 7;
        int READ_WRITE_STORAGE                 = 9;
        int REQUEST_PERMISSION_CODE_VIDEO_CALL = 72;
    }

    interface ImageSelection {

        int CAPTURE_FROM_CAMERA = 301;
        int PICK_FROM_GALLERY   = 302;
    }

    interface SnackBarType {
        int ERROR   = 0;
        int SUCCESS = 1;
        int MESSAGE = 2;
    }

}
