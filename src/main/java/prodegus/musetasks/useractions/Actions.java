package prodegus.musetasks.useractions;

import prodegus.musetasks.lessons.LessonChange;

import java.time.LocalDate;

import static prodegus.musetasks.lessons.LessonModel.*;

public class Actions {
    public static final int ACTION_LESSON_REFRESH = 1;

    public static void processLessonChanges() {
        for (LessonChange lessonChange : getLessonChangeListFromDB()) {
            if (lessonChange.getChangeDate().isBefore(LocalDate.now()) && !lessonChange.isChangeDone()) {
                updateLesson(lessonChange.lesson(), lessonChange.getId());
                lessonChange.setChangeDone(true);
                updateLessonChange(lessonChange);
            }
        }
    }
}
