package in.org.cris.icms.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.org.cris.icms.R;

/**
 * Created by anurag on 9/7/17.
 */
public class ImageSelector extends DialogFragment{
    private final int REQUEST_GALLERY = 1;
    private final int REQUEST_CAMERA = 2;

    public ImageSelector() {
    }

    public static void launchImageSelector(FragmentManager fm){
        ImageSelector imageSelector = new ImageSelector();
        imageSelector.setStyle(STYLE_NO_TITLE, 0);
        imageSelector.show(fm, "imageSelector");
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            dialog.getWindow().setLayout((int)(300*getActivity().getResources().getDisplayMetrics().density + 0.5f), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_image, container, false);
        LinearLayout camera = (LinearLayout)view.findViewById(R.id.camera_linear_layout);
        LinearLayout gallery = (LinearLayout)view.findViewById(R.id.gallery_linear_layout);
        TextView cancel = (TextView)view.findViewById(R.id.cancel_text_view);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(intent, REQUEST_CAMERA);
                dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture:"), REQUEST_GALLERY);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
}
