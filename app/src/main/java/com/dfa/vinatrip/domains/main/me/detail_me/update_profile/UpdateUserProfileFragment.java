package com.dfa.vinatrip.domains.main.me.detail_me.update_profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.TripGuyUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.blurry.Blurry;

import static android.app.Activity.RESULT_OK;
import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PICK_IMAGE;
import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PLACE_AUTO_COMPLETE;

@EFragment(R.layout.fragment_update_user_profile)
public class UpdateUserProfileFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_update_user_profile_tv_percent)
    TextView tvPercent;

    @ViewById(R.id.fragment_update_user_profile_et_nickname)
    EditText etNickname;

    @ViewById(R.id.fragment_update_user_profile_tv_city)
    TextView tvCity;

    @ViewById(R.id.fragment_update_user_profile_et_introduce_your_self)
    EditText etIntroduceYourSelf;

    @ViewById(R.id.fragment_update_user_profile_iv_avatar)
    ImageView ivAvatar;

    @ViewById(R.id.fragment_update_user_profile_iv_blur_avatar)
    ImageView ivBlurAvatar;

    @ViewById(R.id.fragment_update_user_profile_progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.fragment_update_user_profile_spn_sex)
    Spinner spnSex;

    @ViewById(R.id.fragment_update_user_profile_tv_birthday)
    TextView tvBirthday;

    @ViewById(R.id.fragment_update_user_profile_sv_root)
    ScrollView svRoot;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private UserProfile currentUser;
    private Calendar calendar;
    private Uri uri;
    private Bitmap adjustedBitmap;

    @AfterViews
    void init() {
        setContentViews();
    }

    public void setContentViews() {
        adjustedBitmap = null;

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.sex_array, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapter);

        currentUser = dataService.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendar = Calendar.getInstance();

        if (currentUser != null) {
            if (!currentUser.getAvatar().equals("")) {
                Picasso.with(getActivity())
                       .load(currentUser.getAvatar())
                       .into(target);
            }
            etNickname.setText(currentUser.getNickname());
            tvCity.setText(currentUser.getCity());
            if (!currentUser.getBirthday().equals("")) {
                tvBirthday.setText(currentUser.getBirthday());
            } else {
                setCurrentDayForView();
            }
            etIntroduceYourSelf.setText(currentUser.getIntroduceYourSelf());
            switch (currentUser.getSex()) {
                case "Nam":
                    spnSex.setSelection(0);
                    break;
                case "Nữ":
                    spnSex.setSelection(1);
                    break;
                default:
                    spnSex.setSelection(2);
                    break;
            }
        } else {
            setCurrentDayForView();
        }
    }

    public void setCurrentDayForView() {
        // Set current day for tvBirthday
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        tvBirthday.setText(strDate);
    }

    public void uploadUserAvatar() {
        svRoot.scrollTo(0, svRoot.getBottom());
        progressBar.setVisibility(View.VISIBLE);
        TripGuyUtils.setEnableAllViews(svRoot, false);
        tvPercent.setVisibility(View.VISIBLE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayPhoto = baos.toByteArray();

        // Get the path and name photo be upload
        storageReference = FirebaseStorage.getInstance()
                                          .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                                          .child("AvatarProfileUser")
                                          .child(currentUser.getUid() + ".jpg");

        UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
        uploadTask
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        if (isAdded()) {
                            tvPercent.setText(progress + "%");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        TripGuyUtils.setEnableAllViews(svRoot, true);
                        tvPercent.setVisibility(View.GONE);
                        Toasty.error(getActivity(),
                                     "Cập nhật không thành công\nBạn vui lòng thử lại",
                                     Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String linkAvatar;
                        if (downloadUrl == null) linkAvatar = currentUser.getAvatar();
                        else {
                            linkAvatar = downloadUrl.toString();
                        }

                        if (isAdded()) {
                            UserProfile newUserProfile =
                                    new UserProfile(etNickname.getText().toString(),
                                                    linkAvatar,
                                                    etIntroduceYourSelf.getText().toString(),
                                                    tvCity.getText().toString(),
                                                    tvBirthday.getText().toString(),
                                                    currentUser.getUid(),
                                                    spnSex.getSelectedItem().toString(),
                                                    currentUser.getEmail());
                            databaseReference.child("UserProfile").child(currentUser.getUid())
                                             .setValue(newUserProfile);
                            dataService.updateInforCurrentUser(newUserProfile);
                            progressBar.setVisibility(View.GONE);
                            TripGuyUtils.setEnableAllViews(svRoot, true);
                            tvPercent.setVisibility(View.GONE);
                            Toasty.success(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                });
    }

    @Click
    void fragment_update_user_profile_btn_done() {
        // Check if user update info but don't update avatar
        if (uri == null) {
            final UserProfile newUserProfile =
                    new UserProfile(etNickname.getText().toString(),
                                    currentUser.getAvatar(),
                                    etIntroduceYourSelf.getText().toString(),
                                    tvCity.getText().toString(),
                                    tvBirthday.getText().toString(),
                                    currentUser.getUid(),
                                    spnSex.getSelectedItem().toString(),
                                    currentUser.getEmail());

            databaseReference.child("UserProfile").child(currentUser.getUid())
                             .setValue(newUserProfile, new DatabaseReference.CompletionListener() {
                                 @Override
                                 public void onComplete(DatabaseError databaseError,
                                                        DatabaseReference databaseReference) {
                                     if (databaseError != null) {
                                         Toasty.error(getActivity(), "Lỗi đường truyền, bạn hãy gửi lại!",
                                                      Toast.LENGTH_SHORT).show();
                                     } else {
                                         dataService.updateInforCurrentUser(newUserProfile);
                                         Toasty.success(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT)
                                               .show();
                                         getActivity().finish();
                                     }
                                 }
                             });
        } else {
            uploadUserAvatar();
        }
    }

    @Click
    void fragment_update_user_profile_btn_cancel() {
        getActivity().finish();
    }

    @Click
    void fragment_update_user_profile_iv_change_avatar() {
        askPermission();
        if (checkPermission()) {
            Intent intentToLibrary = new Intent();
            intentToLibrary.setType("image/*");
            intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentToLibrary, REQUEST_PICK_IMAGE);
        }
    }

    @Click
    void fragment_update_user_profile_ll_birthday() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                calendar.set(year, month, dayOfMonth);
            }
        };

        // Set current position time when start dialog
        String strDate[] = tvBirthday.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dialog.setTitle("Chọn ngày sinh");
        dialog.show();
    }

    @Click(R.id.fragment_update_user_profile_ll_city)
    void onLlCityClick() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("VN").build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter).build(getActivity());
            startActivityForResult(intent, REQUEST_PLACE_AUTO_COMPLETE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnActivityResult(REQUEST_PICK_IMAGE)
    void onResultImage(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            uri = data.getData();

            // Show avatar be chosen for user first, not upload yet
            Picasso.with(getActivity())
                   .load(uri)
                   .into(target);
        } else {
            Toasty.error(getActivity(), "Không thể chọn được hình, bạn hãy thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    @OnActivityResult(REQUEST_PLACE_AUTO_COMPLETE)
    void onResultPlace(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            tvCity.setText(place.getAddress());
        } else if (data == null) {
            Toasty.error(getActivity(), "Không chọn được địa điểm, bạn hãy thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmap = TripGuyUtils.scaleDown(bitmap, 300, true);
            try {
                String realPath = TripGuyUtils.getRealPath(getActivity(), uri);
                ExifInterface exif = new ExifInterface(realPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                if (orientation != 0f) {
                    Matrix matrix = new Matrix();
                    int rotationInDegrees = exifToDegrees(orientation);
                    matrix.preRotate(rotationInDegrees);
                    adjustedBitmap = Bitmap
                            .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    adjustedBitmap = TripGuyUtils.scaleDown(adjustedBitmap, 300, true);
                } else {
                    adjustedBitmap = bitmap;
                }
            } catch (Exception e) {
            }

            if (adjustedBitmap != null) {
                Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                      .from(adjustedBitmap).into(ivBlurAvatar);
                ivAvatar.setImageBitmap(adjustedBitmap);
            } else {
                Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                      .from(bitmap).into(ivBlurAvatar);
                ivAvatar.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(getActivity(), permissions, 10);
            }
        }
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                                                  Manifest.permission.ACCESS_FINE_LOCATION) ==
               PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(getActivity(),
                                                  Manifest.permission.ACCESS_COARSE_LOCATION) ==
               PackageManager.PERMISSION_GRANTED;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
