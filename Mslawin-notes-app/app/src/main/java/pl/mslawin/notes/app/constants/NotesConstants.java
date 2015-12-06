package pl.mslawin.notes.app.constants;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by maciej on 10/11/15.
 */
public final class NotesConstants {

    public static final String TASKS_LIST_PARAM = "TASKS_LIST_PARAM";
    public static final String TASKS_PARAM = "TASKS_PARAM";
    public static final String USER_EMAIL_PARAM = "USER_EMAIL_PARAM";

    public static final Pattern EMAIL_PATTERN = Patterns.EMAIL_ADDRESS;

    private NotesConstants() {
        // constants class
    }
}
