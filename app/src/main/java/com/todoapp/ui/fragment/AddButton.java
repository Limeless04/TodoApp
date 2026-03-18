package com.todoapp.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.todoapp.R;
import com.todoapp.data.Task;
import com.todoapp.viewmodel.TaskModelView;

public class AddButton extends BottomSheetDialogFragment {

    public static final String TAG = "AddTaskBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        );
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextInputEditText editText = view.findViewById(R.id.editTextTask);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Auto-open keyboard when bottom sheet appears
        editText.requestFocus();

        // Share the same ViewModel as MainActivity
        TaskModelView viewModel = new ViewModelProvider(requireActivity())
                .get(TaskModelView.class);

        btnSave.setOnClickListener(v -> {
            String title = editText.getText() != null
                    ? editText.getText().toString().trim()
                    : "";

            if (title.isEmpty()) {
                textInputLayout.setError("Task cannot be empty");
                return;
            }

            viewModel.insert(new Task(title));
            dismiss();  // closes the bottom sheet
        });

        // Also save when user presses Done on keyboard
        editText.setOnEditorActionListener((v, actionId, event) -> {
            btnSave.performClick();
            return true;
        });
    }
}
