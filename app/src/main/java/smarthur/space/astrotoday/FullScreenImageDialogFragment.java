package smarthur.space.astrotoday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class FullScreenImageDialogFragment extends AppCompatDialogFragment {
    private AppCompatImageView imageView;

    static FullScreenImageDialogFragment newInstance(String imageUrl) {
        FullScreenImageDialogFragment fragment = new FullScreenImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.full_screen_image_dialog_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final DialogFragment fragment = this;
        imageView = view.findViewById(R.id.image);
        Glide.with(fragment).load(fragment.getArguments().getString("url")).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.dismiss();
            }
        });
    }
}