package agis.guillaume.argent.utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DiffCallbackTest {
    private PublishSubject<Integer> onChangeSubject = PublishSubject.create();
    private PublishSubject<Integer> onMoveSubject = PublishSubject.create();
    private PublishSubject<Integer> onInsertedSubject = PublishSubject.create();
    private PublishSubject<Integer> onRemoveSubject = PublishSubject.create();

    private DiffCallback diffCallback = new DiffCallback();

    private TestObserver<Integer> onChangeSubjectTest;
    private TestObserver<Integer> onMoveSubjectTest;
    private TestObserver<Integer> onInsertedSubjectTest;
    private TestObserver<Integer> onRemoveSubjectTest;


    @Before
    public void setup() {
        onChangeSubjectTest = onChangeSubject.test();
        onMoveSubjectTest = onMoveSubject.test();
        onInsertedSubjectTest = onInsertedSubject.test();
        onRemoveSubjectTest = onRemoveSubject.test();
    }

    @After
    public void tearsDown() {
        onChangeSubjectTest.dispose();
        onMoveSubjectTest.dispose();
        onInsertedSubjectTest.dispose();
        onRemoveSubjectTest.dispose();
    }


    @Test
    public void Given_2_equals_lists_then_no_difference_is_found() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        compare(list, list);

        onChangeSubjectTest.assertNoValues();
        onMoveSubjectTest.assertNoValues();
        onInsertedSubjectTest.assertNoValues();
        onRemoveSubjectTest.assertNoValues();
    }

    @Test
    public void Given_a_list_with_a_removed_item_then_find_a_removed_item() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        int position = 1;
        List<String> list2 = new ArrayList<>(list);
        list2.remove(position);

        compare(list, list2);

        onChangeSubjectTest.assertNoValues();
        onMoveSubjectTest.assertNoValues();
        onInsertedSubjectTest.assertNoValues();
        onRemoveSubjectTest.assertValue(position);

    }

    @Test
    public void Given_a_list_with_newitems_then_find_those_new_items() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        List<String> list2 = new ArrayList<>(list);
        list2.add("e");
        list2.add("f");

        compare(list, list2);

        onChangeSubjectTest.assertNoValues();
        onMoveSubjectTest.assertNoValues();
        onInsertedSubjectTest.assertValue(list2.size() - list.size());
        onRemoveSubjectTest.assertNoValues();

    }

    @Test
    public void Given_a_list_with_swap_items_then_swap_those_new_items() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        List<String> list2 = new ArrayList<>();
        list.add("c");
        list.add("b");
        list.add("a");

        compare(list, list2);

        onChangeSubjectTest.assertNoValues();
        onMoveSubjectTest.assertNoValues();
        onInsertedSubjectTest.assertNoValues();
        onRemoveSubjectTest.assertValue(list.size() + list2.size());
    }


    @Test
    public void _Given_a_modified_with_swap_then_find_those_modified_items() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        List<String> list2 = new ArrayList<>();
        list.add("b");
        list.add("a");
        list.add("c");

        compare(list, list2);

        onChangeSubjectTest.assertNoValues();
        onMoveSubjectTest.assertNoValues();
        onInsertedSubjectTest.assertNoValues();
        onRemoveSubjectTest.assertValue(list.size() + list2.size());

    }

    private void compare(List<String> list1, List<String> list2) {

        diffCallback.compareLists(list1, list2);


        DiffUtil.DiffResult diffFound = DiffUtil.calculateDiff(diffCallback);
        diffFound.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                onInsertedSubjectTest.onNext(count);
            }

            @Override
            public void onRemoved(int position, int count) {
                onRemoveSubjectTest.onNext(count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                onMoveSubjectTest.onNext(toPosition - fromPosition);
            }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {
                onChangeSubjectTest.onNext(count);

            }
        });
    }
}