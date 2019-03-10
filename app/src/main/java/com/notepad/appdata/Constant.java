package com.notepad.appdata;

import com.notepad.R;

public interface Constant {
    String EMPTY_STRING = "";

    enum HomeNavigation {
        REMINDER(0, R.id.action_reminders, R.string.reminders),
        TAG(1, R.id.action_tags, R.string.tags),
        HOME(2, R.id.action_home, R.string.home),
        FAVORITE(3, R.id.action_favorite, R.string.favorites),
        CALENDER(4, R.id.action_calender, R.string.calendar);

        public int position;
        public int resourceId;
        public int titleResId;

        HomeNavigation(int position, int resourceId, int titleResId) {
            this.position = position;
            this.resourceId = resourceId;
            this.titleResId = titleResId;
        }

        public static HomeNavigation getItemByPosition(int position) {
            HomeNavigation homeNavigation = null;
            for (HomeNavigation navigation : values()) {
                if (navigation.position == position) {
                    homeNavigation = navigation;
                    break;
                }
            }
            return homeNavigation;
        }

        public static HomeNavigation getItemByResId(int resourceId) {
            HomeNavigation homeNavigation = null;
            for (HomeNavigation navigation : values()) {
                if (navigation.resourceId == resourceId) {
                    homeNavigation = navigation;
                    break;
                }
            }
            return homeNavigation;
        }
    }

    /**
     * The type of file being Saved
     */
    enum FileType {
        ATTACHMENT("Attachment", "");

        public final String extension;
        public final String directory;

        FileType(String relativePath, String extension) {
            this.extension = extension;
            this.directory = relativePath;
        }
    }

    interface DateFormat {
        String LAST_EDITED_TIME_FORMAT = "dd MMM, hh:mm aa";
    }
}
