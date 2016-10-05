package ru.nsu.fit.nsuschedule.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.model.Lesson;

/**
 * Created by Pavel on 24.09.2016.
 */
public class DialogHelper {

    public static AlertDialog getLessonViewDialog(Context context, Lesson lesson){
        View root = View.inflate(context, R.layout.lesson_dialog_layout, null);
        TextView textName = (TextView) root.findViewById(R.id.text_name);
        TextView textTeacher = (TextView) root.findViewById(R.id.text_teacher);
        TextView textRoom = (TextView) root.findViewById(R.id.text_room);

        textName.setText(lesson.getName() + " \"" + getLessonTypeChar(lesson) + "\"");
        textTeacher.setText(lesson.getTeacherId());
        textRoom.setText(lesson.getRoom());

        return new AlertDialog.Builder(context).setView(root).setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
    }

    private static String getLessonTypeChar(Lesson lesson){
        switch (lesson.getType()){
            case LECTURE:
                return "Л";
            case PRACTICUM:
                return "П";
            case SEMINAR:
                return "С";
            default:
                return "";
        }
    }
}
