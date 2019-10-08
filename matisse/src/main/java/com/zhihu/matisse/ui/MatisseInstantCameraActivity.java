package com.zhihu.matisse.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhihu.matisse.internal.entity.SelectionSpec;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import java.util.ArrayList;

import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_SELECTION;
import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_SELECTION_PATH;

public class MatisseInstantCameraActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CAPTURE = 24;
    private MediaStoreCompat mMediaStoreCompat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectionSpec mSpec = SelectionSpec.getInstance();
        if (!mSpec.hasInited) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        mMediaStoreCompat = new MediaStoreCompat(this);
        if (mSpec.captureStrategy == null)
            throw new RuntimeException("Don't forget to set CaptureStrategy.");
        mMediaStoreCompat.setCaptureStrategy(mSpec.captureStrategy);
        mMediaStoreCompat.dispatchCaptureIntent(this, REQUEST_CODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == RESULT_OK) {
            Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
            String path = mMediaStoreCompat.getCurrentPhotoPath();
            ArrayList<Uri> selected = new ArrayList<>();
            selected.add(contentUri);
            ArrayList<String> selectedPath = new ArrayList<>();
            selectedPath.add(path);
            Intent result = new Intent();
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selected);
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPath);
            setResult(RESULT_OK, result);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
